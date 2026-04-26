package gr.pants.pro.edu_analysis.dto;

public record PersonalInfoReadOnlyDTO(

        String identityNumber,

        String placeOfBirth,

        String municipalityOfRegistration
) {
}
