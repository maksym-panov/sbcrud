package com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbstractResponseDto<T> {

    private T payload;

}
