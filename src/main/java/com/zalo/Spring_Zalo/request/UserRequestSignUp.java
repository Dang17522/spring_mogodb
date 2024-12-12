package com.zalo.Spring_Zalo.request;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestSignUp {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String role;
}
