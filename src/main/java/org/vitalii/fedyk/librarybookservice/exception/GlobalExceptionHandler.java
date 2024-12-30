package org.vitalii.fedyk.librarybookservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.vitalii.fedyk.librarybookservice.dto.ExceptionMessageDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionMessageDto handleNotFoundException(RuntimeException e) {
        return getExceptionMessageDto(e);
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionMessageDto handleEmailAlreadyUsedException(RuntimeException e) {
        return getExceptionMessageDto(e);
    }

    @ExceptionHandler(OperationNotPermittedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionMessageDto handleMethodNotAllowed(RuntimeException e) {
        return getExceptionMessageDto(e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessageDto handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        final BindingResult bindingResult = e.getBindingResult();
        final Map<String, Object> errorMessages = new HashMap<>();

        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            errorMessages.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return getExceptionMessageDto(errorMessages);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionMessageDto handleIllegalArgumentException(RuntimeException e) {
        return getExceptionMessageDto(e);
    }

    private ExceptionMessageDto getExceptionMessageDto(Exception e) {
        final ExceptionMessageDto exceptionMessage = new ExceptionMessageDto();
        exceptionMessage.setMessage(e.getMessage());
        return exceptionMessage;
    }

    private ExceptionMessageDto getExceptionMessageDto(Map<String, Object> map) {
        final ExceptionMessageDto exceptionMessage = new ExceptionMessageDto();
        exceptionMessage.additionalInfo(map);
        return exceptionMessage;
    }
}
