package com.example.CloudFile.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    @NonNull
    private String username;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;
}
