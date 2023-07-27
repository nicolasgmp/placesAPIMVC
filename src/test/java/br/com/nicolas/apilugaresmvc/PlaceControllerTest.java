package br.com.nicolas.apilugaresmvc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.nicolas.apilugaresmvc.api.dto.PlaceRequestDTO;
import br.com.nicolas.apilugaresmvc.api.dto.PlaceResponseDTO;
import br.com.nicolas.apilugaresmvc.domain.services.PlaceService;
import br.com.nicolas.apilugaresmvc.web.controllers.PlaceController;

@ExtendWith(MockitoExtension.class)
public class PlaceControllerTest {

  private static final UUID RANDOM_UUID = UUID.randomUUID();

  private static final String NAME = "Teste Name";

  private static final String STATE = "Teste State";

  private static final String CITY = "Teste City";

  private static final String SLUG = "teste-name";

  @CreatedDate
  private static LocalDateTime CREATED_AT;

  @LastModifiedBy
  private static LocalDateTime UPDATED_AT;

  @InjectMocks
  private PlaceController placeController;

  @Mock
  private PlaceService placeService;

  private PlaceRequestDTO placeRequestDTO;

  private PlaceResponseDTO placeResponseDTO;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @BeforeEach
  void createPlaceToTest() {
    placeRequestDTO = new PlaceRequestDTO(NAME, STATE, CITY);
    placeResponseDTO = new PlaceResponseDTO(NAME, SLUG, CITY, STATE, CREATED_AT, UPDATED_AT);
  }

  @Test
  void mustCreatePlaceResponse(){
    when(placeService.createPlace(placeRequestDTO)).thenReturn(placeResponseDTO);

    ResponseEntity<PlaceResponseDTO> response = assertDoesNotThrow(() -> placeController.createPlace(placeRequestDTO));

    assertEquals(ResponseEntity.status(HttpStatus.CREATED).body(placeService.createPlace(placeRequestDTO)), response);
    assertNotNull(response);
    
  }

  @Test
  void mustEditPlaceResponse() {
    when(placeService.editPlace(RANDOM_UUID, placeRequestDTO)).thenReturn(placeResponseDTO);

    ResponseEntity<PlaceResponseDTO> response = assertDoesNotThrow(() -> placeController.editPlace(RANDOM_UUID, placeRequestDTO));

    assertNotNull(response);
    assertNotNull(response.getBody());
    assertEquals(ResponseEntity.class, response.getClass());
    assertEquals(PlaceResponseDTO.class, response.getBody().getClass());
    assertEquals(ResponseEntity.status(HttpStatus.OK).body(placeService.editPlace(RANDOM_UUID, placeRequestDTO)), response);
    
    assertEquals(NAME, response.getBody().name());
    assertEquals(SLUG, response.getBody().slug());
    assertEquals(CITY, response.getBody().city());
    assertEquals(STATE, response.getBody().state());
    assertEquals(CREATED_AT, response.getBody().createdAt());
    assertEquals(UPDATED_AT, response.getBody().updatedAt());

  }

  @Test
  void mustGetPlaceByIdResponse(){
    when(placeService.getPlaceById(RANDOM_UUID)).thenReturn(placeResponseDTO);

    ResponseEntity<PlaceResponseDTO> response = assertDoesNotThrow(() -> placeController.getPlaceById(RANDOM_UUID));
    assertNotNull(response);
    assertNotNull(response.getBody());
    assertEquals(ResponseEntity.class, response.getClass());
    assertEquals(PlaceResponseDTO.class, response.getBody().getClass());
    assertEquals(ResponseEntity.status(HttpStatus.FOUND).body(placeService.getPlaceById(RANDOM_UUID)), response);

    assertEquals(NAME, response.getBody().name());
    assertEquals(SLUG, response.getBody().slug());
    assertEquals(CITY, response.getBody().city());
    assertEquals(STATE, response.getBody().state());
    assertEquals(CREATED_AT, response.getBody().createdAt());
    assertEquals(UPDATED_AT, response.getBody().updatedAt());
  }

  @Test
  void mustGetAllPlaces() {
    when(placeService.getAllPlaces()).thenReturn(List.of(placeResponseDTO));

    var response = assertDoesNotThrow(() -> placeController.getAllPlaces());

    assertNotNull(response);
    assertNotNull(response.getBody());
  }

  @Test
  void mustGetPlaceByPage(){
    when(placeService.getPlacesByPage(5, 10)).thenReturn(List.of(placeResponseDTO));	

    var response = assertDoesNotThrow(() -> placeController.getPlacesByPage(5, 10));

    assertNotNull(response);
    assertNotNull(response.getBody());
  }

  @Test
  void mustGetPlaceByName(){
    when(placeService.getPlaceByName(NAME)).thenReturn(List.of(placeResponseDTO));

    ResponseEntity<List<PlaceResponseDTO>> response = assertDoesNotThrow(() -> placeController.getPlaceByName(NAME));

    assertNotNull(response);
    assertNotNull(response.getBody());
    assertEquals(ResponseEntity.class, response.getClass());
    assertEquals(ResponseEntity.status(HttpStatus.FOUND).body(placeService.getPlaceByName(NAME)), response);

  }

  @Test
  void mustDeletePlace() {
    when(placeService.deletePlace(RANDOM_UUID)).thenReturn("Place deleted Successfully");

    placeService.deletePlace(RANDOM_UUID);

    verify(placeService, times(1)).deletePlace(RANDOM_UUID);
  }

}
