package br.com.nicolas.apilugaresmvc.domain.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.nicolas.apilugaresmvc.domain.entities.PlaceModel;
import jakarta.validation.constraints.NotBlank;

public interface PlaceRepository extends JpaRepository<PlaceModel, UUID> {

  public Optional<List<PlaceModel>> findAllByNameIgnoreCase(String name);

  public Optional<PlaceModel> findByNameAndCityAndStateIgnoreCase(@NotBlank String name, @NotBlank String city,
      @NotBlank String state);
}
