package gr.pants.pro.edu_analysis.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AnalystInsertDTO(

        @NotNull(message = "{NotNull.analystInsertDTO.firstname}")
        @Size(min = 2, message = "{Size.analystInsertDTO.firstname}")
        String firstname,

        @NotNull(message = "{NotNull.analystInsertDTO.lastname}")
        @Size(min = 2, message = "{Size.analystInsertDTO.lastname}")
        String lastname,

        @NotNull(message = "{NotNull.analystInsertDTO.email}")
        String email,

        @NotNull(message = "{NotNull.analystInsertDTO.firmId}")
        Long firmId,

        @Valid
        @NotNull(message = "{NotNull.analystInsertDTO.userInsertDTO}")
        UserInsertDTO userInsertDTO,

        @Valid
        @NotNull(message = "{NotNull.analystInsertDTO.personalInfoInsertDTO}")
        PersonalInfoInsertDTO personalInfoInsertDTO

) {
}
