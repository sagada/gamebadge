package microservices.book.gamification.client.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@ToString
@EqualsAndHashCode
@Getter
@RequiredArgsConstructor
@JsonDeserialize(using = MultiplicationResultAttemptDeserializer.class)
public final class MultiplicationResultAttempt {
    private final String userAlias;
    private final int multiplicationFactorA;
    private final int multiplicationFactorB;
    private final int resultAttempt;
    private final boolean correct;

    MultiplicationResultAttempt(){
        userAlias = null;
        multiplicationFactorA = -1;
        multiplicationFactorB = -1;
        resultAttempt = -1;
        correct = false;
    }

}
