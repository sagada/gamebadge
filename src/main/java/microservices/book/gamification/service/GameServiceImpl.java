package microservices.book.gamification.service;

import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.domain.Badge;
import microservices.book.gamification.domain.BadgeCard;
import microservices.book.gamification.domain.GameStats;
import microservices.book.gamification.domain.ScoreCard;
import microservices.book.gamification.repository.BadgeCardRepository;
import microservices.book.gamification.repository.ScoreCardRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GameServiceImpl implements GameService {

    private final ScoreCardRepository scoreCardRepository;
    private final BadgeCardRepository badgeCardRepository;

    public GameServiceImpl(
            ScoreCardRepository scoreCardRepository
            , BadgeCardRepository badgeCardRepository) {
        this.scoreCardRepository = scoreCardRepository;
        this.badgeCardRepository = badgeCardRepository;
    }

    @Override
    public GameStats newAttemptForUser(Long userId, Long attemptId, boolean correct) {

        // 처음엔 답이 맞았을 때만 점수를 줌
        if (correct)
        {
            ScoreCard scoreCard = new ScoreCard(userId, attemptId);
            scoreCardRepository.save(scoreCard);
            log.info("사용자 ID {}, 점수 {}점, 답안 ID {}", userId, scoreCard.getScore(), attemptId);

            List<BadgeCard> badgeCardList = processForBadges(userId, attemptId);


        }
    }

    /**
     * 조건이 충족될 경우 새 배지를 제공하기 위해 얻은 총 점수와 점수 카드를 확인
     */
    private List<BadgeCard> processForBadges(final Long userId, final Long attemptId)
    {

        List<BadgeCard> badgeCards = new ArrayList<>();
        int totalScore = scoreCardRepository.getTotalScoreForUser(userId);
        log.info("사용자 ID {} 의 새로운 점수 {}", userId, totalScore);

        List<ScoreCard> scoreCardList = scoreCardRepository.findByUserIdOrderByScoreTimeStampDesc(userId);
        List<BadgeCard> badgeCardList = badgeCardRepository.findByUserIdOrderByBadgeTimestampDesc(userId);
        // 점수 기반 배지
        Optional<BadgeCard> badgeCard = checkAndGiveGadgeBaseOnScore(badgeCardList, Badge.BRONZE_MULTIPLICATOR, totalScore, 100, userId);
        badgeCard.ifPresent(badgeCards::add);
    }

    /**
     * 배지를 얻기 위한 조건을 넘는지 체크하는 편의성 메서드
     * 또한 조건이 충족되면 사용자에게 배지를 부여
     */
    private Optional<BadgeCard> checkAndGiveGadgeBaseOnScore(
            final List<BadgeCard> badgeCards
            , final Badge badge
            , final int score
            , final int scoreThrehold
            , final Long userId)
    {
        if (score >= scoreThrehold && !containsBadge(badgeCards, badge))
        {
            return Optional.of(giveBadgeToUser(badge, userId));
        }

        return Optional.empty();
    }

    /**
     * 주어진 사용자에게 새로운 배지를 부여하는 메서드
     */
    private BadgeCard giveBadgeToUser(Badge badge, Long userId)
    {
        BadgeCard badgeCard = new BadgeCard(userId, badge);
        badgeCardRepository.save(badgeCard);
        log.info("사용자 ID {} 새로운 배지 획득: {}", userId, badge);
        return badgeCard;
    }

    /**
     * 배지 목록에 해당 배지가 포함돼 있는지 확인하는 메서드
     */
    private boolean containsBadge(List<BadgeCard> badgeCards, Badge badge)
    {
        return badgeCards.stream().anyMatch(b -> b.getBadge().equals(badge));
    }

    @Override
    public GameStats retrieveStatsForUser(Long userId) {
        return null;
    }
}
