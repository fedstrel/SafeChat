package com.example.safechat.dto;

import javax.validation.constraints.NotEmpty;

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
    private String email;
    private String info;
}
