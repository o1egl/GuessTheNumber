package org.telegram.guessnum.bot.response;


import lombok.Data;
import org.telegram.guessnum.bot.model.User;

@Data
public class GetMeResponse extends Response {
    private User result;
}
