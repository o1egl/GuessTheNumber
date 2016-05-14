package org.telegram.guessnum.bot.request;


public class UpdatesRequest extends Request {

    public UpdatesRequest(long offset, int timeout){
        setParameter("offset", offset);
        setParameter("timeout", timeout);
    }

    @Override
    public String getMethod() {
        return "getUpdates";
    }

}
