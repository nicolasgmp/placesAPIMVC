package br.com.nicolas.apilugaresmvc.domain.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.nicolas.apilugaresmvc.domain.entities.Place;

public interface PlaceRepository extends JpaRepository<Place, UUID> {

  public List<Place> findAllByNameIgnoreCase(String name);

  public List<Place> findByNameIgnoreCase(String name);

  public List<Place> findByCityIgnoreCase(String city);

  public List<Place> findByStateIgnoreCase(String state);

}
