package gr.pants.pro.edu_analysis.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record AnalystUpdateDTO(

        @NotNull
        UUID uuid,

        @NotNull
        @Size(min = 2)
        String firstname,

        @NotNull
        @Size(min = 2)
        String lastname,

        @NotNull
        String email,

        @NotNull
        Long firmId,

        @Valid
        @NotNull
        UserInsertDTO userInsertDTO,

        @Valid
        @NotNull
        PersonalInfoInsertDTO personalInfoInsertDTO
) {
}
