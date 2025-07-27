package br.com.idus.chronos.service.exceptions;

import org.springframework.http.HttpStatus;

public class ConflictException extends BaseApplicationException {
    public ConflictException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
}
