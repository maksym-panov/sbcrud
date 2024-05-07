package com.maksympanov.hneu.mjt.sbcrud.sbcrud.exception;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.BackendErrorCode;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.MessageResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MessageResponseDto handleNotFoundException(NotFoundException ex) {
        return MessageResponseDto.failure(BackendErrorCode.ENTITY_NOT_FOUND, ex.getMessage());
    }

}
