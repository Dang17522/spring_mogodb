package com.zalo.Spring_Zalo.Entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Document(collection = "product_multi_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductMultiImage implements Serializable {
    @Transient
    public static final String SEQUENCE_NAME = "product_multi_image_sequence";
    @Id
    private Integer id;
    private String image;
    private String publicId;
    @DBRef
    private Product product;

}
