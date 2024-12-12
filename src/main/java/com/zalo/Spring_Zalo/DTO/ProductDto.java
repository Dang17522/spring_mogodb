package com.zalo.Spring_Zalo.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class ProductDto {
    private int id;
    private String name;
    private int status;
    private String image;
    private int point;
    private LocalDateTime createAt;
    private String publicId;
}
