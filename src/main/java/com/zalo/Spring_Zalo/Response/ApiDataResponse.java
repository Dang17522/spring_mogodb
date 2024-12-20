package com.zalo.Spring_Zalo.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiDataResponse {
    private String message;
    private boolean success;
    private int status;
    private Object data;
}
