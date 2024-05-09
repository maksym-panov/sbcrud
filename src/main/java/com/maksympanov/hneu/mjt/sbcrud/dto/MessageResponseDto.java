package com.maksympanov.hneu.mjt.sbcrud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MessageResponseDto {

    private String resolution;

    private BackendErrorCode errorCode;

    private String errorMessage;

    public static MessageResponseDto success() {
        return new MessageResponseDto("SUCCESS");
    }

    public static MessageResponseDto failure(BackendErrorCode errorCode, String errorMessage) {
        return new MessageResponseDto("FAILED", errorCode, errorMessage);
    }

    private MessageResponseDto(String resolution) {
        this.resolution = resolution;
    }

    private MessageResponseDto(String resolution, BackendErrorCode errorCode, String errorMessage) {
        this.resolution = resolution;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}
