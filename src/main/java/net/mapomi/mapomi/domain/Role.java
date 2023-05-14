package net.mapomi.mapomi.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    DISABLED("ROLE_DISABLED", "장애인"), ABLED("ROLE_ABLED","동행인"), OBSERVER("ROLE_OBSERVER","지인");
    private final String key;
    private final String title;

    public static Role setRole(String type){
        if(type.equals("disabled"))
            return DISABLED;
        else if(type.equals("abled"))
            return ABLED;
        else
            return OBSERVER;
    }
}
