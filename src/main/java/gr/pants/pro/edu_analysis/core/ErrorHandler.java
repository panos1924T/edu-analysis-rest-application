package gr.pants.pro.edu_analysis.core;

import gr.pants.pro.edu_analysis.core.exceptions.ValidationException;
import gr.pants.pro.edu_analysis.dto.ValidationErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ValidationErrorResponseDTO> handleValidationException(ValidationException e) {
        log.warn("Validation failed! Message={}", e.getMessage());
        BindingResult bindingResult = e.getBindingResult();

        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return new ResponseEntity<>(new ValidationErrorResponseDTO(
                e.getCode(), e.getMessage(), errors), HttpStatus.BAD_REQUEST);
    }
}
