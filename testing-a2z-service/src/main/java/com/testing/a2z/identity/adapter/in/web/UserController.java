package com.testing.a2z.identity.adapter.in.web;

import com.testing.a2z.identity.password.validation.InvalidPasswordException;
import com.testing.a2z.identity.password.validation.PasswordValidationError.ContainsIllegalCharacter;
import com.testing.a2z.identity.password.validation.PasswordValidationError.InsufficientOccurrences;
import com.testing.a2z.identity.password.validation.PasswordValidationError.TooShort;
import com.testing.a2z.identity.port.in.register.RegisterUserCommand;
import com.testing.a2z.identity.port.in.register.RegisterUserUseCase;
import com.testing.a2z.identity.port.in.register.UsernameTakenException;
import com.testing.a2z.identity.port.in.verify.VerifyPasswordQuery;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@AllArgsConstructor
public class UserController {

    private final VerifyPasswordQuery verifyPasswordQuery;
    private final RegisterUserUseCase registerUserUseCase;

    @GetMapping("/verify")
    public boolean verify(@Validated @NotBlank @RequestParam String username,
                          @Validated @NotBlank @RequestParam String password) {
        return verifyPasswordQuery.verify(username, password);
    }

    @PostMapping("/register")
    public RegistrationResponse register(@Validated @RequestBody RegistrationRequest request) {
        var command = new RegisterUserCommand(request.username(), request.password());
        var user = registerUserUseCase.register(command);
        return new RegistrationResponse(user.id().toString());
    }

    @ExceptionHandler(UsernameTakenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String mapUsernameTakenException() {
        return "Username already taken";
    }

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String mapInvalidPasswordReason(InvalidPasswordException exception) {
        var passwordValidationError = exception.getPasswordValidationError();
        return switch (passwordValidationError) {
            case TooShort tooShort -> "Password is too short";
            case ContainsIllegalCharacter containsIllegalCharacter -> "Password contains an illegal character";
            case InsufficientOccurrences insufficientOccurrences -> "Password has insufficient occurences of " + insufficientOccurrences.type();
        };
    }

}
