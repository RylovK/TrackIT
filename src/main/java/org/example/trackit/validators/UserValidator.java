package org.example.trackit.validators;

import lombok.AllArgsConstructor;
import org.example.trackit.dto.LoginDTO;
import org.example.trackit.entity.User;
import org.example.trackit.services.impl.UserService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@AllArgsConstructor
public class UserValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LoginDTO user = (LoginDTO) target;
        userService.findUserByUsername(user.getUsername())
                .ifPresentOrElse(_ -> errors.rejectValue("username", "duplicate", "Username already exists"),
                        () -> {});
    }
}
