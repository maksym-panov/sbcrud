package com.maksympanov.hneu.mjt.sbcrud.dto.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrdersDto {

    private List<CustomerOrderDto> orders;

}
