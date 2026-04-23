package gr.pants.pro.edu_analysis.dto;

public record ErrorResponseDTO(
        String code,
        String message
) {
    public ErrorResponseDTO(String code) {
        this(code, "");
    }
}
