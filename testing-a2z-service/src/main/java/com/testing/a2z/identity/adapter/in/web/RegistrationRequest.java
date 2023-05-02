package com.testing.a2z.identity.adapter.in.web;

import jakarta.validation.constraints.NotBlank;

public record RegistrationRequest(@NotBlank String username,
                                  @NotBlank String password) {

}
