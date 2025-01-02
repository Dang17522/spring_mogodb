package com.zalo.Spring_Zalo.Entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product implements Serializable {
    @Transient
    public static final String SEQUENCE_NAME = "product_sequence";
    @Id
    private Integer id;
    private String name;
    private int status;
    private String image;
    private int quantity;
    private String description;
    private int vote;
    private Integer price;
    private LocalDateTime createAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private String publicId;

    @DBRef
    private Category category;

}
