package com.example.CloudFile.models;

import com.example.CloudFile.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users", schema = "public")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true, nullable = false)
    private String login;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public UserDTO toDTO() {
        return new UserDTO(this.login);
    }
}