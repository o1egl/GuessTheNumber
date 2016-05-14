package org.telegram.guessnum.bot.model;

import com.google.common.base.Joiner;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class User {

    private int id;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_Name")
    private String lastName;
    private String username;

    public String getFullName(){
        return Joiner.on(" ").skipNulls().join(getFirstName(), getLastName());
    }
}
