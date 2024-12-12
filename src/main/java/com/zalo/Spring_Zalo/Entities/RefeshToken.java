package com.zalo.Spring_Zalo.Entities;

import java.io.Serializable;
import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "refesh_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefeshToken implements Serializable {
    @Transient
    public static final String SEQUENCE_NAME = "refesh_token_sequence";
    @Id
    private Integer id;
    private String refreshToken;
    private Instant expiryDate;
    private User user;

}
