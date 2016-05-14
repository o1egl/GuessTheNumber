package org.telegram.guessnum.bot.model;


import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Message {

    @SerializedName("message_id")
    private long id;
    private User from;
    private long date;
    private Chat chat;

    @SerializedName("forward_from")
    private User forwardFrom;

    @SerializedName("forward_from_chat")
    private Chat forwardFromChat;

    @SerializedName("forward_date")
    private long forwardDate;

    @SerializedName("reply_to_message")
    private Message replyToMessage;

    private String text;

    @SerializedName("new_chat_member")
    private User newChatMember;

    @SerializedName("left_chat_member")
    private User leftChatMember;




}
