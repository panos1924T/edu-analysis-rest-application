package gr.pants.pro.edu_analysis.dto;

import java.util.Map;

public record ValidationErrorResponseDTO(
        String code,
        String message,
        Map<String, String> errors
) {
}
