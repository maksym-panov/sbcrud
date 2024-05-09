package com.maksympanov.hneu.mjt.sbcrud.dto.order;

import com.maksympanov.hneu.mjt.sbcrud.model.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeOrderStatusDto {

    @NotNull
    private OrderStatus newStatus;

}
