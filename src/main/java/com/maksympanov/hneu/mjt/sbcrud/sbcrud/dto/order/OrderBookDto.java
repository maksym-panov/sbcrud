package com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderBookDto {

    private String id;

    private String bookName;

    private BigDecimal pricePerBook;

    private Integer quantity;

    private BigDecimal summary;

}
