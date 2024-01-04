package br.com.nicolas.apilugaresmvc.api.dto;

import br.com.nicolas.apilugaresmvc.domain.entities.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record RegisterDTO(@NotBlank @NotEmpty String username, @NotBlank @NotEmpty String password, UserRole role) {

}
