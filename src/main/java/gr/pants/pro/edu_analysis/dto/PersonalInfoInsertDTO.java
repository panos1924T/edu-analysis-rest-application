package gr.pants.pro.edu_analysis.dto;

import jakarta.validation.constraints.NotBlank;

public record PersonalInfoInsertDTO(

        @NotBlank(message = "{NotBlank.personalInfoInsertDTO.identityNumber}")
        String identityNumber,

        @NotBlank(message = "{NotBlank.personalInfoInsertDTO.placeOfBirth}")
        String placeOfBirth,

        @NotBlank(message = "{NotBlank.personalInfoInsertDTO.municipalityOfRegistration}")
        String municipalityOfRegistration
) {
}
