package org.telegram.guessnum.bot.model;


import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Chat {

    public enum Type {
        @SerializedName("private")PRIVATE,
        @SerializedName("group")GROUP,
        @SerializedName("supergroup")SUPERGROUP,
        @SerializedName("channel")CHANNEL
    }

    private long id;
    private Type type;
    private String title;
    private String username;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_Name")
    private String lastName;

}
