package com.zalo.Spring_Zalo.DTO;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto implements Serializable {
    private int id;
    private String username;
    private String fullname;
    private String email;
    private String avatar;
    private String password;
    private String company;
    private int companyId;
    private int status;
    private String role;
    private Integer roleId;
    private LocalDateTime createAt;
}
