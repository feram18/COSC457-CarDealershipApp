package edu.towson.cosc457.CarDealership.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such entity") // 404 ERROR
public class NotFoundException extends RuntimeException {
    private final HttpStatus status;

    public NotFoundException(final String entity, final Long entityId, HttpStatus status) {
        super(MessageFormat.format("Could not find {0} with id: {1}", entity, entityId));
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
