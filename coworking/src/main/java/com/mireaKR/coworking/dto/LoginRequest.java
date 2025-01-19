package com.mireaKR.coworking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Требуется электронная почта")
    private String email;
    @NotBlank(message = "Требуется ввести пароль")
    private String password;
}