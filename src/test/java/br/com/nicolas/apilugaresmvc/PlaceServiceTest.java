package br.com.nicolas.apilugaresmvc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.github.slugify.Slugify;

import br.com.nicolas.apilugaresmvc.api.dto.PlaceRequestDTO;
import br.com.nicolas.apilugaresmvc.api.dto.PlaceResponseDTO;
import br.com.nicolas.apilugaresmvc.domain.entities.Place;
import br.com.nicolas.apilugaresmvc.domain.exceptions.DataIntegrityViolationException;
import br.com.nicolas.apilugaresmvc.domain.exceptions.PlaceNotFoundException;
import br.com.nicolas.apilugaresmvc.domain.repositories.PlaceRepository;
import br.com.nicolas.apilugaresmvc.domain.services.PlaceService;
import br.com.nicolas.apilugaresmvc.web.maps.PlaceMapper;

public class PlaceServiceTest {

  private static final String CITY_IN_THIS_STATE_ALREADY_INCLUDED_IN_OUR_DATABASE = "City in this state already included in our database";

  private static final String PLACE_NOT_FOUND = "Place not found in our database";

  @InjectMocks
  private PlaceService service;

  @Mock
  private PlaceRepository repository;

  @Mock
  private Slugify slg;

  private static final UUID RANDOM_UUID = UUID.randomUUID();

  private static final String NAME = "Teste Name";

  private static final String STATE = "Teste State";

  private static final String CITY = "Teste City";

  private static final String SLUG = "teste-name";

  @CreatedDate
  private static LocalDateTime CREATED_AT;

  @LastModifiedBy
  private static LocalDateTime UPDATED_AT;

  private PlaceRequestDTO placeRequestDTO;

  private Optional<Place> optionalPlace;

  private Place place;

  @BeforeEach
  void setUp() {
    placeRequestDTO = new PlaceRequestDTO(NAME, STATE, CITY);
    optionalPlace = Optional.of(new Place(RANDOM_UUID, NAME, SLUG, CITY, STATE, CREATED_AT, UPDATED_AT));
    place = new Place(RANDOM_UUID, NAME, SLUG, CITY, STATE, CREATED_AT, UPDATED_AT);
    MockitoAnnotations.openMocks(this);
  }

  // * Tests with success

  @Test
  void mustCreatePlaceSuccess() {
    when(repository.save(place)).thenReturn(place);

    var response = assertDoesNotThrow(() -> service.createPlace(placeRequestDTO));

    assertNotNull(response);
    assertEquals(PlaceResponseDTO.class, response.getClass());
    assertEquals(PlaceMapper.fromPlaceToResponse(place), response);
    assertEquals(NAME, response.name());
    assertEquals(SLUG, response.slug());
    assertEquals(CITY, response.city());
    assertEquals(STATE, response.state());
    assertEquals(CREATED_AT, response.createdAt());
    assertEquals(UPDATED_AT, response.updatedAt());

  }

  @Test
  void mustEditPlaceSuccess() {
    when(repository.findById(RANDOM_UUID)).thenReturn(optionalPlace);
    when(repository.save(place)).thenReturn(place);

    var response = service.editPlace(RANDOM_UUID, placeRequestDTO);
    
    assertNotNull(response);
    assertEquals(PlaceResponseDTO.class, response.getClass());
    assertEquals(PlaceMapper.fromPlaceToResponse(place), response);
    assertEquals(NAME, response.name());
    assertEquals(SLUG, response.slug());
    assertEquals(CITY, response.city());
    assertEquals(STATE, response.state());
    assertEquals(CREATED_AT, response.createdAt());
    assertEquals(UPDATED_AT, response.updatedAt());

    verify(repository).findById(RANDOM_UUID);    
    verify(repository).save(place);
  }

  @Test
  void mustGetPlaceByIdSuccess(){
    when(repository.findById(RANDOM_UUID)).thenReturn(optionalPlace);

    var response = service.getPlaceById(RANDOM_UUID);

    assertNotNull(response);
    assertEquals(PlaceResponseDTO.class, response.getClass());
    assertEquals(PlaceMapper.fromPlaceToResponse(place), response);
    assertEquals(NAME, response.name());
    assertEquals(SLUG, response.slug());
    assertEquals(CITY, response.city());
    assertEquals(STATE, response.state());
    assertEquals(CREATED_AT, response.createdAt());
    assertEquals(UPDATED_AT, response.updatedAt());

    verify(repository).findById(RANDOM_UUID);
  }

  @Test
  void mustGetAllPlaces(){
    when(repository.findAll()).thenReturn(List.of(place));

    var response = service.getAllPlaces();

    assertNotNull(response);
    assertEquals(repository.findAll().stream().map(PlaceMapper::fromPlaceToResponse).toList(), response);
  }

  @Test
  void mustGetPlaceByPage() {
    when(repository.findAll(PageRequest.of(5, 10))).thenReturn(new PageImpl<>(List.of(place), PageRequest.of(5, 10), List.of(place).size()));

    var response = service.getPlacesByPage(5, 10);

    assertNotNull(response);
    assertTrue(response instanceof List);
  }

  @Test
  void mustGetPlaceByName(){
    when(repository.findAllByNameIgnoreCase(NAME)).thenReturn(List.of(place));

    var response = service.getPlaceByName(NAME);

    assertNotNull(response);
    assertTrue(response instanceof List);
    assertTrue(response.get(0) instanceof PlaceResponseDTO);

    verify(repository).findAllByNameIgnoreCase(NAME);
  }

  @Test
  void mustDeletePlace() {
    when(repository.findById(RANDOM_UUID)).thenReturn(optionalPlace);
    doNothing().when(repository).delete(place);

    var response = service.deletePlace(RANDOM_UUID);

    verify(repository, times(1)).delete(any());
    assertNotNull(response);
    assertEquals(String.class, response.getClass());
    assertEquals("Place deleted successfully", response);

    verify(repository).findById(RANDOM_UUID);
    verify(repository).delete(place);
  }

  @Test
  void mustFindByIdNotThrowing(){
    when(repository.findById(RANDOM_UUID)).thenReturn(optionalPlace);

    var response = assertDoesNotThrow(() -> service.findById(RANDOM_UUID));

    assertNotNull(response);
    assertEquals(Place.class, response.getClass());
    assertEquals(RANDOM_UUID, response.getId());
    assertEquals(NAME, response.getName());
    assertEquals(SLUG, response.getSlug());
    assertEquals(CITY, response.getCity());
    assertEquals(STATE, response.getState());
    assertEquals(CREATED_AT, response.getCreatedAt());
    assertEquals(UPDATED_AT, response.getUpdatedAt());

    verify(repository).findById(RANDOM_UUID);

  }

  // * Tests with error/throw

  @Test
  void mustFindByIdReturningAnPlaceNotFoundException(){
    when(repository.findById(RANDOM_UUID)).thenThrow(new PlaceNotFoundException(PLACE_NOT_FOUND));

    try{
      service.findById(RANDOM_UUID);
    } catch(Exception e){
      assertEquals(PlaceNotFoundException.class, e.getClass());
      assertEquals(PLACE_NOT_FOUND, e.getMessage());
    }

  }

  @Test
  void mustFindByNameAndCityAndStateIgnoringCase(){
    when(repository.findByNameIgnoreCase(NAME))
    .thenThrow(
      new DataIntegrityViolationException(CITY_IN_THIS_STATE_ALREADY_INCLUDED_IN_OUR_DATABASE));

    when(repository.findByCityIgnoreCase(CITY))
    .thenThrow(
      new DataIntegrityViolationException(CITY_IN_THIS_STATE_ALREADY_INCLUDED_IN_OUR_DATABASE));
      
    when(repository.findByStateIgnoreCase(STATE))
    .thenThrow(
      new DataIntegrityViolationException(CITY_IN_THIS_STATE_ALREADY_INCLUDED_IN_OUR_DATABASE));

    try{
      service.findByNameAndCityAndStateIgnoreCase(placeRequestDTO);
    } catch(Exception e){
      assertEquals(DataIntegrityViolationException.class, e.getClass());
      assertEquals(CITY_IN_THIS_STATE_ALREADY_INCLUDED_IN_OUR_DATABASE, e.getMessage());
    }

  }

  @Test
  void mustCreateThenReturnAnDataIntegrityViolationException(){
    when(repository.findByNameIgnoreCase(NAME))
    .thenThrow(
      new DataIntegrityViolationException(CITY_IN_THIS_STATE_ALREADY_INCLUDED_IN_OUR_DATABASE));

    when(repository.findByCityIgnoreCase(CITY))
    .thenThrow(
      new DataIntegrityViolationException(CITY_IN_THIS_STATE_ALREADY_INCLUDED_IN_OUR_DATABASE));
      
    when(repository.findByStateIgnoreCase(STATE))
    .thenThrow(
      new DataIntegrityViolationException(CITY_IN_THIS_STATE_ALREADY_INCLUDED_IN_OUR_DATABASE));

    try{
      service.createPlace(placeRequestDTO);
    } catch(Exception e){
      assertEquals(DataIntegrityViolationException.class, e.getClass());
      assertEquals(CITY_IN_THIS_STATE_ALREADY_INCLUDED_IN_OUR_DATABASE, e.getMessage());
    }
  }

  @Test
  void mustEditThenReturnAnDataIntegrityViolationException(){
    when(repository.findByNameIgnoreCase(NAME))
    .thenThrow(
      new DataIntegrityViolationException(CITY_IN_THIS_STATE_ALREADY_INCLUDED_IN_OUR_DATABASE));

    when(repository.findByCityIgnoreCase(CITY))
    .thenThrow(
      new DataIntegrityViolationException(CITY_IN_THIS_STATE_ALREADY_INCLUDED_IN_OUR_DATABASE));
      
    when(repository.findByStateIgnoreCase(STATE))
    .thenThrow(
      new DataIntegrityViolationException(CITY_IN_THIS_STATE_ALREADY_INCLUDED_IN_OUR_DATABASE));

    try{
      service.editPlace(RANDOM_UUID, placeRequestDTO);
    } catch(Exception e){
      assertEquals(DataIntegrityViolationException.class, e.getClass());
      assertEquals(CITY_IN_THIS_STATE_ALREADY_INCLUDED_IN_OUR_DATABASE, e.getMessage());
    }
  }

  void mustDeleteWithPlaceNotFoundException(){
    when(repository.findById(RANDOM_UUID)).thenThrow(new PlaceNotFoundException(PLACE_NOT_FOUND));

    try{
      service.deletePlace(RANDOM_UUID);
    } catch(Exception e){
      assertEquals(PlaceNotFoundException.class, e.getClass());
      assertEquals(PLACE_NOT_FOUND, e.getMessage());
    }
  }

}
