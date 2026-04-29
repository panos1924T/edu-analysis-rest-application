package gr.pants.pro.edu_analysis.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AnalystUpdateDTO(

        @NotNull(message = "{NotNull.analystUpdateDTO.uuid}")
        UUID uuid,

        @NotNull(message = "{NotNull.analystUpdateDTO.firstname}")
        @Size(min = 2, message = "{Size.analystUpdateDTO.firstname}")
        String firstname,

        @NotNull(message = "{NotNull.analystUpdateDTO.lastname}")
        @Size(min = 2, message = "{Size.analystUpdateDTO.lastname}")
        String lastname,

        @NotNull(message = "{NotNull.analystUpdateDTO.email}")
        String email,

        @NotNull(message = "{NotNull.analystUpdateDTO.firmId}")
        Long firmId,

        @Valid
        @NotNull(message = "{NotNull.analystUpdateDTO.userUpdateDTO}")
        UserUpdateDTO userUpdateDTO,

        @Valid
        @NotNull(message = "{NotNull.analystUpdateDTO.personalInfoUpdateDTO}")
        PersonalInfoUpdateDTO personalInfoUpdateDTO
) {
}
