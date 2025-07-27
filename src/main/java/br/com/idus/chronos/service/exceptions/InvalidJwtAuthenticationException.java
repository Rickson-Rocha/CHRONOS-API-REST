package br.com.idus.chronos.service.exceptions;

import org.springframework.http.HttpStatus;


public class InvalidJwtAuthenticationException extends BaseApplicationException {
    public InvalidJwtAuthenticationException(String msg) {
        super(msg);
    }
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
}
