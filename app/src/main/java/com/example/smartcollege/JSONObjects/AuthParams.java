package com.example.smartcollege.JSONObjects;

public class AuthParams extends BodyParams{

    public AuthParams(String username, String password) {
        super.addParam("username",username);
        super.addParam("password",password);
    }
}
