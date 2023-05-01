package com.testing.a2z.identity.adapter.in.web;

import com.testing.a2z.identity.port.in.verify.VerifyPasswordQuery;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {

    private final VerifyPasswordQuery verifyPasswordQuery;

    @GetMapping("/verify")
    public boolean verify(@Validated @NotBlank @RequestParam String username,
                          @Validated @NotBlank @RequestParam String password) {
        return verifyPasswordQuery.verify(username, password);
    }

}
