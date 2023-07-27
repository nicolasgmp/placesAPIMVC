package br.com.nicolas.apilugaresmvc.domain.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.slugify.Slugify;

import br.com.nicolas.apilugaresmvc.api.dto.PlaceRequestDTO;
import br.com.nicolas.apilugaresmvc.api.dto.PlaceResponseDTO;
import br.com.nicolas.apilugaresmvc.domain.entities.Place;
import br.com.nicolas.apilugaresmvc.domain.exceptions.DataIntegrityViolationException;
import br.com.nicolas.apilugaresmvc.domain.exceptions.PlaceNotFoundException;
import br.com.nicolas.apilugaresmvc.domain.repositories.PlaceRepository;
import br.com.nicolas.apilugaresmvc.web.maps.PlaceMapper;

@Service
public class PlaceService {

  private PlaceRepository placeRepository;
  private Slugify slg;

  public PlaceService(PlaceRepository placeRepository) {
    this.placeRepository = placeRepository;
    slg = Slugify.builder().build();
  }

  public PlaceResponseDTO createPlace(PlaceRequestDTO placeRequestDTO) {
    findByNameAndCityAndStateIgnoreCase(placeRequestDTO);
    var place = new Place(
        null, placeRequestDTO.name(), slg.slugify(placeRequestDTO.name()), placeRequestDTO.city(),
        placeRequestDTO.state(), null, null);
    placeRepository.save(place);
    return PlaceMapper.fromPlaceToResponse(place);
  }

  public PlaceResponseDTO editPlace(UUID id, PlaceRequestDTO placeRequestDTO) {
    findByNameAndCityAndStateIgnoreCase(placeRequestDTO);
    var place = findById(id);
    BeanUtils.copyProperties(placeRequestDTO, place);
    place.setSlug(slg.slugify(placeRequestDTO.name()));
    placeRepository.save(place);
    return PlaceMapper.fromPlaceToResponse(place);
  }

  public PlaceResponseDTO getPlaceById(UUID id) {
    var place = findById(id);
    return PlaceMapper.fromPlaceToResponse(place);
  }

  public List<PlaceResponseDTO> getAllPlaces() {
    return placeRepository.findAll().stream().map(PlaceMapper::fromPlaceToResponse).toList();
  }

  public List<PlaceResponseDTO> getPlacesByPage(Integer page, Integer qtyUsers) {
    if (qtyUsers >= 5 || qtyUsers <= 0 || qtyUsers == null) {
      qtyUsers = 5;
    }
    if (page < 0 || page == null) {
      page = 0;
    }

    Pageable pageRequest = PageRequest.of(page, qtyUsers);
    Page<Place> placePage = placeRepository.findAll(pageRequest);

    if (placePage == null || placePage.isEmpty()) {
      return Collections.emptyList();
    }

    return placePage
        .map(PlaceMapper::fromPlaceToResponse)
        .toList();
  }

  public List<PlaceResponseDTO> getPlaceByName(String name) {
    List<Place> place = placeRepository.findAllByNameIgnoreCase(name);
    return place.stream().map(PlaceMapper::fromPlaceToResponse).toList();
  }

  public String deletePlace(UUID id) {
    var place = findById(id);
    placeRepository.delete(place);
    return "Place deleted successfully";
  }

  public Place findById(UUID id) {
    Optional<Place> place = placeRepository.findById(id);
    return place.orElseThrow(() -> new PlaceNotFoundException("Place not found in our database"));
  }

  public void findByNameAndCityAndStateIgnoreCase(PlaceRequestDTO placeRequestDTO) {
    var sameName = placeRepository.findByNameIgnoreCase(placeRequestDTO.name());
    var sameCity = placeRepository.findByCityIgnoreCase(placeRequestDTO.city());
    var sameState = placeRepository.findByStateIgnoreCase(placeRequestDTO.state());

    if (!sameName.isEmpty() && !sameCity.isEmpty() && !sameState.isEmpty()) {
      throw new DataIntegrityViolationException(
          "City in this state already included in our database");
    }
  }

}
