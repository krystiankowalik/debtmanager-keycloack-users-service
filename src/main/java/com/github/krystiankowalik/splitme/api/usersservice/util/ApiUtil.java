package com.github.krystiankowalik.splitme.api.usersservice.util;

public class ApiUtil {

    public static String getIdFromUri(String location){
        String[] locationArray = location
                .split("/");

        return locationArray[locationArray.length - 1];

    }
}
