package org.telegram.guessnum.bot.response;


import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.telegram.guessnum.bot.model.Message;

@Data
public class UpdatesResult {
    @SerializedName("update_id")
    private long updateId;
    private Message message;
}
