package com.jdev.customers.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerUpdateDTO (
        @Email @NotBlank String email,
        @NotBlank String address,
        @NotBlank String phone,
        @NotBlank String country) {}

