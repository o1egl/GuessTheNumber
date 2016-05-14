package org.telegram.guessnum.bot.request;


import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class Request {

    private final Map<String, Object> parameters = new HashMap<>();

    public void setParameter(String key, Object val){
        parameters.put(key, val);
    }

    public abstract String getMethod();
}
