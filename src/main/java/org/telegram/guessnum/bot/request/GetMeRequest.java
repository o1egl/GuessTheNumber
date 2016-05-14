package org.telegram.guessnum.bot.request;


public class GetMeRequest extends Request {
    @Override
    public String getMethod() {
        return "getMe";
    }
}
