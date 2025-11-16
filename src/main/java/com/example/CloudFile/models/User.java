package com.example.CloudFile.models;

import com.example.CloudFile.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "users", schema = "public")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true, nullable = false)
    @NonNull
    private String login;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NonNull
    private String password;

    public UserDTO toDTO() {
        return new UserDTO(this.login);
    }
}
