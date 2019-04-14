package com.example.smartcollege;

public class AuthParams {
    String password;
    String username;

    public AuthParams(String username, String password) {
        this.password = password;
        this.username = username;
    }

    public String getPassword() {
        return password;
    }


    public String getUsername() {
        return username;
    }
}
