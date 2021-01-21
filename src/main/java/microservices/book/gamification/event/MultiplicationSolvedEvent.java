package microservices.book.gamification.event;

import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class MultiplicationSolvedEvent implements Serializable {

    private  Long multiplicationResultAttemptId;
    private  Long userId;
    private  boolean correct;

    public MultiplicationSolvedEvent(Long multiplicationResultAttemptId, Long userId, boolean correct) {
        this.multiplicationResultAttemptId = multiplicationResultAttemptId;
        this.userId = userId;
        this.correct = correct;
    }

}