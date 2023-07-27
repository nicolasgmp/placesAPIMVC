package br.com.nicolas.apilugaresmvc.domain.exceptions;

public class PlaceNotFoundException extends RuntimeException {

  public PlaceNotFoundException(String message) {
    super(message);
  }

}
