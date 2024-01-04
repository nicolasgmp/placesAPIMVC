package br.com.nicolas.apilugaresmvc.domain.exceptions;

public class DuplicatePlaceException extends DataIntegrityViolationException {

    public DuplicatePlaceException(String message) {
        super(message);
    }
}
