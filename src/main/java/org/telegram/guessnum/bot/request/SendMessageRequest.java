package org.telegram.guessnum.bot.request;


public class SendMessageRequest extends Request {

    public SendMessageRequest(long chatId, String text){
        setParameter("chat_id", chatId);
        setParameter("text", text);
    }

    @Override
    public String getMethod() {
        return "sendMessage";
    }
}
