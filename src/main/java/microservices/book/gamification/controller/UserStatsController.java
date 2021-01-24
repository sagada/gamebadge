package microservices.book.gamification.controller;

import lombok.extern.slf4j.Slf4j;
import microservices.book.gamification.domain.GameStats;
import microservices.book.gamification.service.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/stats")
public class UserStatsController {
    private final GameService gameService;

    public UserStatsController(GameService gameService)
    {
        this.gameService = gameService;
    }

    @GetMapping
    public GameStats getStatsForUser(@RequestParam("userId") final Long userId)
    {
        log.info("userId : {} ", userId);
        return gameService.retrieveStatsForUser(userId);
    }
}
