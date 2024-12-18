package com.zalo.Spring_Zalo.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestImport {

    private String username;
    private String fullname;
    private String email;
    private String password;
    private String role;

}


