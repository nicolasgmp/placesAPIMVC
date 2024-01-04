package br.com.nicolas.apilugaresmvc.domain.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.github.slugify.Slugify;

import br.com.nicolas.apilugaresmvc.api.dto.PlaceRequestDTO;
import br.com.nicolas.apilugaresmvc.api.dto.PlaceResponseDTO;
import br.com.nicolas.apilugaresmvc.domain.entities.PlaceModel;
import br.com.nicolas.apilugaresmvc.domain.exceptions.DataIntegrityViolationException;
import br.com.nicolas.apilugaresmvc.domain.exceptions.PlaceNotFoundException;
import br.com.nicolas.apilugaresmvc.domain.repositories.PlaceRepository;
import br.com.nicolas.apilugaresmvc.web.maps.PlaceMapper;
import jakarta.transaction.Transactional;

@Service
public class PlaceService {

    private PlaceRepository placeRepository;
    private Slugify slg;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
        slg = Slugify.builder().build();
    }

    public PlaceModel findById(UUID id) {
        Optional<PlaceModel> place = placeRepository.findById(id);
        return place.orElseThrow(() -> new PlaceNotFoundException("Place not found in our database"));
    }

    @Transactional
    public PlaceResponseDTO createPlace(PlaceRequestDTO placeRequestDTO) {
        findByNameAndCityAndStateIgnoreCase(placeRequestDTO);
        PlaceModel place = new PlaceModel(
                placeRequestDTO.name(), slg.slugify(placeRequestDTO.name()),
                placeRequestDTO.city(), placeRequestDTO.state());
        placeRepository.save(place);
        return PlaceMapper.fromPlaceToResponse(place);
    }

    @Transactional
    public PlaceResponseDTO editPlace(UUID id, PlaceRequestDTO placeRequestDTO) {
        findByNameAndCityAndStateIgnoreCase(placeRequestDTO);
        PlaceModel place = findById(id);
        place.setName(placeRequestDTO.name());
        place.setSlug(slg.slugify(placeRequestDTO.name()));
        placeRepository.save(place);
        return PlaceMapper.fromPlaceToResponse(place);
    }

    @Transactional
    public String deletePlace(UUID id) {
        PlaceModel place = findById(id);
        placeRepository.delete(place);
        return "Place deleted successfully";
    }

    public PlaceResponseDTO getPlaceById(UUID id) {
        PlaceModel place = findById(id);
        return PlaceMapper.fromPlaceToResponse(place);
    }

    public List<PlaceResponseDTO> getAllPlaces() {
        return placeRepository.findAll().stream().map(PlaceMapper::fromPlaceToResponse).collect(Collectors.toList());
    }

    public List<PlaceResponseDTO> getPlacesByPage(Integer page, Integer qtyUsers) {
        if (qtyUsers >= 5 || qtyUsers <= 0 || qtyUsers == null) {
            qtyUsers = 5;
        }
        if (page < 0 || page == null) {
            page = 0;
        }

        Pageable pageRequest = PageRequest.of(page, qtyUsers, Sort.by("id"));
        Page<PlaceModel> placePage = placeRepository.findAll(pageRequest);

        if (placePage == null || placePage.isEmpty()) {
            return Collections.emptyList();
        }

        return placePage
                .map(PlaceMapper::fromPlaceToResponse)
                .toList();
    }

    public List<PlaceResponseDTO> getPlaceByName(String name) {
        List<PlaceModel> place = placeRepository
                .findAllByNameIgnoreCase(name)
                .orElseThrow(() -> new PlaceNotFoundException("There isn't any places with that name in our database"));

        return place.stream().map(PlaceMapper::fromPlaceToResponse).collect(Collectors.toList());
    }

    public void findByNameAndCityAndStateIgnoreCase(PlaceRequestDTO placeRequestDTO) {
        var sameNameCityState = placeRepository.findByNameAndCityAndStateIgnoreCase(
                placeRequestDTO.name(), placeRequestDTO.city(), placeRequestDTO.state());

        if (sameNameCityState.isPresent()) {
            throw new DataIntegrityViolationException("City in this state already included in our database");
        }
    }

}
