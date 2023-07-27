package br.com.nicolas.apilugaresmvc.web.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.nicolas.apilugaresmvc.api.dto.PlaceRequestDTO;
import br.com.nicolas.apilugaresmvc.api.dto.PlaceResponseDTO;
import br.com.nicolas.apilugaresmvc.domain.services.PlaceService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/places")
public class PlaceController {

  private PlaceService placeService;

  public PlaceController(PlaceService placeService) {
    this.placeService = placeService;
  }

  @PostMapping
  public ResponseEntity<PlaceResponseDTO> createPlace(@Valid @RequestBody PlaceRequestDTO placeRequestDTO) {
    return ResponseEntity.status(HttpStatus.CREATED).body(placeService.createPlace(placeRequestDTO));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<PlaceResponseDTO> editPlace(@PathVariable("id") UUID id,
      @Valid @RequestBody PlaceRequestDTO placeRequestDTO) {
    return ResponseEntity.status(HttpStatus.OK).body(placeService.editPlace(id, placeRequestDTO));
  }

  @GetMapping("/{id}")
  public ResponseEntity<PlaceResponseDTO> getPlaceById(@PathVariable("id") UUID id) {
    return ResponseEntity.status(HttpStatus.FOUND).body(placeService.getPlaceById(id));
  }

  @GetMapping("/{page}/{qtyUsers}")
  public ResponseEntity<List<PlaceResponseDTO>> getPlacesByPage(
      @PathVariable("page") Integer page, @PathVariable("qtyUsers") Integer qtyUsers) {
    return ResponseEntity.status(HttpStatus.FOUND).body(placeService.getPlacesByPage(page, qtyUsers));
  }

  @GetMapping
  public ResponseEntity<List<PlaceResponseDTO>> getAllPlaces() {
    return ResponseEntity.status(HttpStatus.FOUND).body(placeService.getAllPlaces());
  }

  @GetMapping("/name")
  public ResponseEntity<List<PlaceResponseDTO>> getPlaceByName(@RequestParam("name") String name) {
    return ResponseEntity.status(HttpStatus.FOUND).body(placeService.getPlaceByName(name));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deletePlace(@PathVariable("id") UUID id) {
    return ResponseEntity.status(HttpStatus.OK).body(placeService.deletePlace(id));
  }

}
