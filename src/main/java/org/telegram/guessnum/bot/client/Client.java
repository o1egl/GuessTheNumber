package org.telegram.guessnum.bot.client;


import org.telegram.guessnum.bot.request.Request;
import org.telegram.guessnum.bot.response.Response;

public interface Client {
    <T extends Request, R extends Response> R execute(T req, Class<R> clazz);
}
