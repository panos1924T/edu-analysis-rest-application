package gr.pants.pro.edu_analysis.dto;

import jakarta.validation.constraints.NotBlank;

public record PersonalInfoUpdateDTO(

        @NotBlank(message = "{NotBlank.personalInfoUpdateDTO.identityNumber}")
        String identityNumber,

        @NotBlank(message = "{NotBlank.personalInfoUpdateDTO.placeOfBirth}")
        String placeOfBirth,

        @NotBlank(message = "{NotBlank.personalInfoUpdateDTO.municipalityOfRegistration}")
        String municipalityOfRegistration
) {
}
