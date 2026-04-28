package gr.pants.pro.edu_analysis.validator;


import gr.pants.pro.edu_analysis.dto.UserInsertDTO;
import gr.pants.pro.edu_analysis.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserInsertValidator implements Validator {

    private final IUserService userService;


    @Override
    public boolean supports(Class<?> clazz) {
        return UserInsertDTO.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserInsertDTO userInsertDTO = (UserInsertDTO) target;

        if (userService.isUserExists(userInsertDTO.username())) {
            log.warn("Save failed. User with username={} already exists", userInsertDTO.username());
            errors.rejectValue(
                    "username",
                    "username.user.exists",
                    "Username already exists"
            );
        }
    }
}
