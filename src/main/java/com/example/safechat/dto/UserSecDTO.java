package com.example.safechat.dto;

import com.example.safechat.annotation.ValidEmail;
import com.example.safechat.entity.enums.ERole;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserSecDTO {
    private Long id;
    @NotEmpty
    private String firstname;
    @NotEmpty
    private String lastname;
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    @NotEmpty
    @ValidEmail
    private String email;
    @NotEmpty
    private ERole role;
    private String info;
}
