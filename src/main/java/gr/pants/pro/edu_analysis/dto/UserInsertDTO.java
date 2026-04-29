package gr.pants.pro.edu_analysis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserInsertDTO(

        @NotNull(message = "{NotNull.userInsertDTO.username}")
        @Size(min = 3, max = 20, message = "{Size.userInsertDTO.username}")
        String username,

        @NotNull(message = "{NotNull.userInsertDTO.password}")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])^.{8,}$", message = "{Pattern.userInsertDTO.password}")
        String password,

        @NotNull(message = "{NotNull.userInsertDTO.roleId}")
        Long roleId
) {
}
