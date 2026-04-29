package gr.pants.pro.edu_analysis.dto;

import jakarta.validation.constraints.NotNull;

public record AuthenticationRequestDTO(
        @NotNull(message = "{NotNull.authenticationRequestDTO.username}") String username,
        @NotNull(message = "{NotNull.authenticationRequestDTO.password}") String password
) {
}
