package com.zalo.Spring_Zalo.Entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "others")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orther implements Serializable {
    @Transient
    public static final String SEQUENCE_NAME = "others_sequence";
    @Id
    private Integer id;
    private int status;
    private LocalDateTime createAt;
    @DBRef
    private User user;
    
    @DBRef
    private List<Cart> carts;

}
