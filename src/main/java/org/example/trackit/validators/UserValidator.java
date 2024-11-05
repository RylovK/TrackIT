package org.example.trackit.validators;

import lombok.AllArgsConstructor;
import org.example.trackit.dto.LoginDTO;
import org.example.trackit.entity.User;
import org.example.trackit.services.UserService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@AllArgsConstructor
public class UserValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        LoginDTO user = (LoginDTO) target;
        User founded = userService.findUserByUsername(user.getUsername());
        if (founded != null) {
            errors.rejectValue("username", "duplicate", "Username already exists");
        }
    }
}
