package com.zalo.Spring_Zalo.Entities;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Roles implements Serializable {
    @Transient
    public static final String SEQUENCE_NAME = "roles_sequence";
    @Id
    private Integer id;
    private String name;
    private LocalDateTime createAt;
    private LocalDateTime updatedAt;

    @DBRef
    private User user;

}
