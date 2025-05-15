package com.example.FullStackBlogSite.user;

public class LoginRequest {
    private String username;
    private String password;

    public String getPassword(){
        return  password;
    }

    public String getUsername(){
        return  username;
    }
}