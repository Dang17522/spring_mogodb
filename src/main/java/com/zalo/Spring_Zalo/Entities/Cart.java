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

@Document(collection = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart implements Serializable {
    @Transient
    public static final String SEQUENCE_NAME = "carts_sequence";
    @Id
    private Integer id;
    private int quantity;
    private Double totalPrice;
    private LocalDateTime createAt;

    @DBRef
    private User user;

    @DBRef
    private Product product;

}
