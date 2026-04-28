package gr.pants.pro.edu_analysis.validator;

import gr.pants.pro.edu_analysis.core.exceptions.EntityNotFoundException;
import gr.pants.pro.edu_analysis.dto.AnalystReadOnlyDTO;
import gr.pants.pro.edu_analysis.dto.AnalystUpdateDTO;
import gr.pants.pro.edu_analysis.service.IAnalystSevice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
@Slf4j
public class AnalystEditValidator implements Validator {

    private final IAnalystSevice analystSevice;

    @Override
    public boolean supports(Class<?> clazz) {
        return AnalystUpdateDTO.class == clazz;
    }

    @Override
    public void validate(Object target, Errors errors) {
        AnalystUpdateDTO analystUpdateDTO = (AnalystUpdateDTO) target;

        try {
            AnalystReadOnlyDTO readOnlyDTO = analystSevice.getAnalystByUuidAndDeletedFalse(analystUpdateDTO.uuid());

            if (readOnlyDTO != null && !readOnlyDTO.email().equals(analystUpdateDTO.email())) {
                if (analystSevice.isAnalystExistsByEmail(analystUpdateDTO.email())) {
                    log.warn("Update failed. Analyst with email={} already exists", analystUpdateDTO.email());
                    errors.rejectValue("email", "email.analyst.exists");
                }
            }
        } catch (EntityNotFoundException e) {
            log.warn("Update failed. Analyst with uuid={} not found", analystUpdateDTO.uuid());
            errors.rejectValue("uuid", "uuid.analyst.notfound");
        }
    }
}
