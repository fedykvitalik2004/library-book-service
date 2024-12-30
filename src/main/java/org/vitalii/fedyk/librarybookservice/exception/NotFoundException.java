package org.vitalii.fedyk.librarybookservice.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(final String message) {
        super(message);
    }
}
