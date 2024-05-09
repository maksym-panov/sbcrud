package com.maksympanov.hneu.mjt.sbcrud.dto.order;

import com.maksympanov.hneu.mjt.sbcrud.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrderDto {

    private String id;

    private OrderStatus status;

    private List<OrderBookDto> orderBooks;

    private BigDecimal total;

}
