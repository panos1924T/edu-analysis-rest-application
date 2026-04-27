package gr.pants.pro.edu_analysis.validator;

import gr.pants.pro.edu_analysis.dto.AnalystInsertDTO;
import gr.pants.pro.edu_analysis.service.AnalystService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnalystInsertValidator implements Validator {

    private final AnalystService analystService;

    @Override
    public boolean supports(Class<?> clazz) {
        return AnalystInsertDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AnalystInsertDTO insertDTO = (AnalystInsertDTO) target;

        if (insertDTO.email() != null && analystService.isAnalystExistsByEmail(insertDTO.email())) {
            log.warn("Save failed. Analyst with email={} already exists!", insertDTO.email());
            errors.rejectValue("Email", "analyst.email.exists",
                    "Analyst with email=" + insertDTO.email() + " already exists.");
        }
    }
}
