package com.zalo.Spring_Zalo.Response;

import com.zalo.Spring_Zalo.Entities.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    public ApiResponse(String string, boolean b, int i, User user) {
        //TODO Auto-generated constructor stub
    }
    public ApiResponse(String string, boolean b, int i, User user, String refreshToken, String accessToken) {
        //TODO Auto-generated constructor stub
    }
    private String message;
    private boolean success;
    private int status;
}
