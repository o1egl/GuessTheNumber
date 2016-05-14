package org.telegram.guessnum.game;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.telegram.guessnum.bot.model.User;

@Getter
@Setter
@AllArgsConstructor
public class GameRoom {
    private GameState gameState;
    private User winner;
    private int number;

}
