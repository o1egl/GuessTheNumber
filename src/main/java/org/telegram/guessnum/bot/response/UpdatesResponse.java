package org.telegram.guessnum.bot.response;


import lombok.Data;

import java.util.List;

@Data
public class UpdatesResponse extends Response {
    private List<UpdatesResult> result;
}
