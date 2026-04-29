package gr.pants.pro.edu_analysis.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(

        @NotNull(message = "{NotNull.userUpdateDTO.username}")
        @Size(min = 3, max = 20, message = "{Size.userUpdateDTO.username}")
        String username,

        @NotNull(message = "{NotNull.userUpdateDTO.password}")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])^.{8,}$", message = "{Pattern.userUpdateDTO.password}")
        String password
) {
}
