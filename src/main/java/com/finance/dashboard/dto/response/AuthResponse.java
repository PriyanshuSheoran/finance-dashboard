package com.finance.dashboard.dto.response;

public class AuthResponse {

    private String token;
    private String tokenType = "Bearer";
    private UserResponse user;

    public AuthResponse() {}

    public AuthResponse(String token, UserResponse user) {
        this.token = token;
        this.user  = user;
    }

    public String getToken()             { return token; }
    public void setToken(String v)       { this.token = v; }
    public String getTokenType()         { return tokenType; }
    public void setTokenType(String v)   { this.tokenType = v; }
    public UserResponse getUser()        { return user; }
    public void setUser(UserResponse v)  { this.user = v; }
}
