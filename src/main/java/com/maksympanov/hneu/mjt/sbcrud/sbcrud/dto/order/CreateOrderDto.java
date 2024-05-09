package com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderDto {

    @Valid
    @NotEmpty
    private List<CreateUpdateOrderBookDto> orderBooks;

}
