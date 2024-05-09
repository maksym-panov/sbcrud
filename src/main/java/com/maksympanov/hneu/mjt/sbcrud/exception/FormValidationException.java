package com.maksympanov.hneu.mjt.sbcrud.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Collectors;

public class FormValidationException extends RuntimeException {

    public FormValidationException(List<ObjectError> errors) {
        super(
            errors.stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(", "))
        );
    }

}
