package com.maksympanov.hneu.mjt.sbcrud.dao;

import com.maksympanov.hneu.mjt.sbcrud.exception.NotFoundException;
import com.maksympanov.hneu.mjt.sbcrud.model.CustomerOrder;
import com.maksympanov.hneu.mjt.sbcrud.model.OrderBook;
import com.maksympanov.hneu.mjt.sbcrud.model.OrderStatus;
import com.maksympanov.hneu.mjt.sbcrud.model.ServiceUser;
import com.maksympanov.hneu.mjt.sbcrud.repository.CustomerOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerOrderDaoTest {

    @Mock
    private CustomerOrderRepository customerOrderRepository;

    @InjectMocks
    private CustomerOrderDao customerOrderDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should create new order")
    void shouldCreateOrder() {
        // Given
        var actorUser = new ServiceUser();
        var orderBooks = List.of(new OrderBook());
        var expectedOrder = new CustomerOrder();

        when(customerOrderRepository.save(any())).thenReturn(expectedOrder);

        // When
        var createdOrder = customerOrderDao.createOrder(actorUser, orderBooks);

        // Then
        assertThat(createdOrder).isNotNull();
        assertThat(expectedOrder).isEqualTo(createdOrder);
        verify(customerOrderRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Should get order by ID (Throwable)")
    void shouldGetOrderByIdThrowable() {
        // Given
        var id = UUID.randomUUID();
        var expectedOrder = CustomerOrder.builder().id(id).build();
        when(customerOrderRepository.findById(id)).thenReturn(Optional.of(expectedOrder));

        // When
        var foundOrder = customerOrderDao.getOrderByIdThrowable(id);

        // Then
        assertThat(foundOrder).isNotNull();
        assertThat(expectedOrder).isEqualTo(foundOrder);
        verify(customerOrderRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should throw exception if there is no such order")
    void shouldThrowExceptionIfNoSuchOrder() {
        // Given
        var notExistingId = UUID.randomUUID();
        var errorMessage = "Could not find CustomerOrder with id: %s".formatted(notExistingId);

        // Then
        assertThatThrownBy(() -> customerOrderDao.getOrderByIdThrowable(notExistingId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(errorMessage);
    }

    @Test
    @DisplayName("Should get all orders of user")
    void shouldGetAllOrdersOfUser() {
        // Given
        var user = new ServiceUser();
        var orders = List.of(new CustomerOrder());
        when(customerOrderRepository.findAllByUserId(user.getId())).thenReturn(orders);

        // When
        var resultOrders = customerOrderDao.getAllOrdersOfUser(user);

        // Then
        assertThat(resultOrders).isNotNull();
        assertThat(resultOrders).isEqualTo(orders);
        verify(customerOrderRepository, times(1)).findAllByUserId(user.getId());
    }

    @Test
    @DisplayName("Should get orders pageable")
    void shouldGetOrdersPageable() {
        // Given
        var pageNumber = 0;
        var pageSize = 10;
        var orders = new ArrayList<CustomerOrder>();
        orders.add(new CustomerOrder());
        var expectedPage = new PageImpl<>(orders);

        when(customerOrderRepository.findAll(any(PageRequest.class))).thenReturn(expectedPage);

        // When
        var resultPage = customerOrderDao.getOrders(pageNumber, pageSize);

        // Then
        assertThat(resultPage).isNotNull();
        assertThat(expectedPage).isEqualTo(resultPage);

        var pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("dateCreated"));
        verify(customerOrderRepository, times(1)).findAll(pageRequest);
    }

    @Test
    @DisplayName("Should update order with status")
    void shouldUpdateOrderWithStatus() {
        // Given
        var order = new CustomerOrder();
        var status = OrderStatus.SHIPPING;
        when(customerOrderRepository.save(any())).thenReturn(order);

        // When
        var updatedOrder = customerOrderDao.updateWithStatus(order, status);

        // Then
        assertThat(updatedOrder).isNotNull();
        assertThat(order.getStatus()).isEqualTo(status);
        verify(customerOrderRepository, times(1)).save(order);
    }

    @Test
    @DisplayName("Should update order with order books")
    void shouldUpdateOrderWithOrderBooks() {
        // Given
        var order = new CustomerOrder();
        var orderBooks = List.of(new OrderBook());
        when(customerOrderRepository.save(any())).thenReturn(order);

        // When
        var updatedOrder = customerOrderDao.updateWithOrderBooks(order, orderBooks);

        // Then
        assertThat(updatedOrder).isNotNull();
        assertThat(updatedOrder.getOrderBooks()).isEqualTo(orderBooks);
        verify(customerOrderRepository, times(1)).save(order);
    }
}