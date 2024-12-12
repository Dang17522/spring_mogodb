package com.zalo.Spring_Zalo.Entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    @Transient
    public static final String SEQUENCE_NAME = "users_sequence";
    @Id
    private Integer id;
    private String username;
    private String fullname;
    private String email;
    private String avatar;
    private String publicId;
    private String password;
    private int status = 1;
    private LocalDateTime createAt;
    private LocalDateTime updatedAt;

    @DBRef
    @JsonIgnore
    private Roles role;

    @DBRef
    @JsonIgnore
    private List<Cart> cart;

    @DBRef
    @JsonIgnore
    private List<Orther> orthers;

    public String getRoleName() {
        return role != null ? role.getName() : null;
    }

}
