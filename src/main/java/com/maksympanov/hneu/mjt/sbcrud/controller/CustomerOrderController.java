package com.maksympanov.hneu.mjt.sbcrud.controller;

import com.maksympanov.hneu.mjt.sbcrud.dto.AbstractResponseDto;
import com.maksympanov.hneu.mjt.sbcrud.dto.MessageResponseDto;
import com.maksympanov.hneu.mjt.sbcrud.dto.order.*;
import com.maksympanov.hneu.mjt.sbcrud.auth.AuthContextHolder;
import com.maksympanov.hneu.mjt.sbcrud.config.APIPrefixes;
import com.maksympanov.hneu.mjt.sbcrud.exception.FormValidationException;
import com.maksympanov.hneu.mjt.sbcrud.service.CustomerOrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
public class CustomerOrderController {

    private AuthContextHolder authContextHolder;

    private CustomerOrderService customerOrderService;

    private DataMapper dataMapper;

    @GetMapping(APIPrefixes.USER + "/orders")
    public AbstractResponseDto<CustomerOrdersDto> getOrdersOfUser() {
        var userId = authContextHolder.getCurrentUser().getId();
        var orders = customerOrderService.getAllOrdersByUserId(userId);
        var rv = dataMapper.mapCustomerOrdersDto(orders);
        return new AbstractResponseDto<>(rv);
    }

    @GetMapping(APIPrefixes.VENDOR + "/orders")
    public AbstractResponseDto<CustomerOrdersDto> getOrdersListByVendor(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize
    ) {
        if (pageNumber == null || pageSize == null || pageNumber <= 0 || pageSize <= 0) {
            pageNumber = 1;
            pageSize = 20;
        }
        var ordersPage = customerOrderService.getOrdersListPageable(pageNumber, pageSize);
        var rv = dataMapper.mapCustomerOrdersDto(ordersPage);
        return new AbstractResponseDto<>(rv);
    }

    @GetMapping(APIPrefixes.USER + "/orders/{orderId}")
    public AbstractResponseDto<CustomerOrderDto> getOrderByUser(@PathVariable String orderId) {
        var id = UUID.fromString(orderId);
        var userId = authContextHolder.getCurrentUser().getId();
        log.info("getOrderByUser: orderId - {}, userId - {}", orderId, userId);
        var order = customerOrderService.getOrderInfoIfBelongsToUser(id, userId);
        var rv = dataMapper.mapCustomerOrderDto(order);
        return new AbstractResponseDto<>(rv);
    }

    @GetMapping(APIPrefixes.VENDOR + "/orders/{orderId}")
    public AbstractResponseDto<CustomerOrderDto> getOrderInfoByVendor(@PathVariable String orderId) {
        log.info("getOrderInfoByVendor: orderId - {}", orderId);
        var id = UUID.fromString(orderId);
        var order = customerOrderService.getOrderInfo(id);
        var rv = dataMapper.mapCustomerOrderDto(order);
        return new AbstractResponseDto<>(rv);
    }

    @PostMapping(APIPrefixes.USER + "/orders")
    public AbstractResponseDto<CustomerOrderDto> createNewOrderByUser(
            @Valid @RequestBody CreateOrderDto dto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("createNewOrderByUser: found validation errors in request body: {}", bindingResult.getAllErrors());
            throw new FormValidationException(bindingResult.getAllErrors());
        }
        var userId = authContextHolder.getCurrentUser().getId();
        var newOrder = customerOrderService.postNewOrderByUser(userId, dto.getOrderBooks());
        var rv = dataMapper.mapCustomerOrderDto(newOrder);
        return new AbstractResponseDto<>(rv);
    }

    @PutMapping(APIPrefixes.VENDOR + "/orders/{orderId}/status")
    public AbstractResponseDto<MessageResponseDto> changeOrderStatusByVendor(
            @PathVariable String orderId,
            @Valid @RequestBody ChangeOrderStatusDto dto,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("changeOrderStatusByVendor: found validation errors in request body: {}", bindingResult.getAllErrors());
            throw new FormValidationException(bindingResult.getAllErrors());
        }
        log.info("changeOrderStatusByVendor: changing status of order {} to {}", orderId, dto.getNewStatus());
        var id = UUID.fromString(orderId);
        customerOrderService.changeOrderStatus(id, dto.getNewStatus());
        return new AbstractResponseDto<>(MessageResponseDto.success());
    }

    @PutMapping(APIPrefixes.VENDOR + "/orders/{orderId}/items")
    public AbstractResponseDto<CustomerOrderDto> updateOrderItemsByVendor(
        @PathVariable String orderId,
        @Valid @RequestBody ChangeOrderBooksDto dto,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            log.warn("updateOrderItemsByVendor: found validation errors in request body: {}", bindingResult.getAllErrors());
            throw new FormValidationException(bindingResult.getAllErrors());
        }
        var id = UUID.fromString(orderId);
        var newOrder = customerOrderService.updateOrderWithNewItems(id, dto.getNewOrderBooks());
        var rv = dataMapper.mapCustomerOrderDto(newOrder);
        return new AbstractResponseDto<>(rv);
    }

}
