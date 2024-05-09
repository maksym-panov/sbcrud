package com.maksympanov.hneu.mjt.sbcrud.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeOrderBooksDto {

    private List<CreateUpdateOrderBookDto> newOrderBooks;

}
