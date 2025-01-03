package com.zalo.Spring_Zalo.DTO;

import com.zalo.Spring_Zalo.Entities.ProductMultiImage;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ProductDto {
    private int id;
    private String name;
    private int status;
    private int quantity;
    private Integer price;
    private String description;
    private int vote;
    private LocalDateTime createAt;
    private String publicId;
    private Integer category;
    private List<ProductMultiImage> productMultiImage;
}
