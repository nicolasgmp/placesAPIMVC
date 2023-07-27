package br.com.nicolas.apilugaresmvc.api.dto;

import java.time.LocalDateTime;

public record PlaceResponseDTO(
    String name, String slug, String city, String state,
    LocalDateTime createdAt, LocalDateTime updatedAt) {

}
