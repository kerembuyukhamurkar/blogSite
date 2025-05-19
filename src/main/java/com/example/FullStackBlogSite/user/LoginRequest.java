package com.example.FullStackBlogSite.user;

import jakarta.validation.constraints.NotEmpty;

public class LoginRequest {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;

    public String getPassword(){
        return  password;
    }

    public String getUsername(){
        return  username;
    }
}