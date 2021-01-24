package microservices.book.gamification.repository;

import microservices.book.gamification.domain.LeaderBoardRow;
import microservices.book.gamification.domain.ScoreCard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/*
 * ScoreCard CRUD 작업 처리
 */
public interface ScoreCardRepository extends CrudRepository<ScoreCard, Long> {

    /**
     * ScoreCard 의 점수를 합해서 사용자의 총 점수를 조
     * @param userId 총 점수를 조회하고자 하는 사용자의 ID
     * @return 사용자의 총 점수
     */
    @Query("SELECT SUM(s.score) FROM microservices.book.gamification.domain.ScoreCard s WHERE s.userId = :userId GROUP BY s.userId")
    int getTotalScoreForUser(@Param("userId") final Long userId);
    /**
     * 사용자와 사용자의 총 점수를 나타내는 {@link LeaderBoardRow} 리스트를 조회
     * @return 높은 점수 순으로 정렬된 리더보드
     */
    @Query("select new microservices.book.gamification.domain.LeaderBoardRow(s.userId, SUM(s.score)) " +
            "FROM microservices.book.gamification.domain.ScoreCard s " +
            "GROUP BY s.userId ORDER BY SUM(s.score) DESC")
    List<LeaderBoardRow> findFirst10();

    /**
     * 사용자의 모든 ScoreBoard 를 조회
     * @param userId 사용자 ID
     * @return 특정 사용자의 최근순으로 정렬된 ScoreBoard 리스트
     */
    List<ScoreCard> findByUserIdOrderByScoreTimeStampDesc(final Long userId);
}
