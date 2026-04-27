package gr.pants.pro.edu_analysis.dto;

import jakarta.validation.constraints.NotBlank;

public record PersonalInfoUpdateDTO(

        @NotBlank
        String identityNumber,

        @NotBlank
        String placeOfBirth,

        @NotBlank
        String municipalityOfRegistration
) {
}
