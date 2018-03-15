package com.example.phoenix.b2cuseraccess.models;

/**
 * Created by Phoenix on 14-Aug-17.
 */

public class UserSession {
    private static String email;

    public static void setSession(String str) {
        email = str;
    }

    public static String getSession() {
        return email;
    }
}
