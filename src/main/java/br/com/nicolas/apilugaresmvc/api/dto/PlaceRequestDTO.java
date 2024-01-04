package br.com.nicolas.apilugaresmvc.api.dto;

import jakarta.validation.constraints.NotBlank;

public record PlaceRequestDTO(@NotBlank String name, @NotBlank String state, @NotBlank String city) {

}
