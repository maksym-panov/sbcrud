package com.maksympanov.hneu.mjt.sbcrud.dto.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdateOrderBookDto {

    @NotEmpty
    private String bookId;

    @Positive
    @NotNull
    private Integer quantity;

}
