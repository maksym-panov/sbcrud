package com.maksympanov.hneu.mjt.sbcrud.service;

import com.maksympanov.hneu.mjt.sbcrud.dao.BookDao;
import com.maksympanov.hneu.mjt.sbcrud.dao.GenreDao;
import com.maksympanov.hneu.mjt.sbcrud.dao.ServiceUserDao;
import com.maksympanov.hneu.mjt.sbcrud.dto.order.CreateUpdateOrderBookDto;
import com.maksympanov.hneu.mjt.sbcrud.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.maksympanov.hneu.mjt.sbcrud.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomerOrderServiceTest {

    @Autowired
    private CustomerOrderService customerOrderService;

    @Autowired
    private ServiceUserDao serviceUserDao;

    @Autowired
    private BookDao bookDao;

    @Autowired
    private GenreDao genreDao;

    private ServiceUser testUser;

    private Book testBook1;

    private Book testBook2;

    private Book testBook3;

    @BeforeEach
    void setUp() {
        testUser = serviceUserDao.createNewUser(
                getRandomAlphabeticalString(10) + "@maksympanov.com",
                "passwordHash",
                getRandomNumericString(10),
                "Maksym",
                "Panov",
                UserRole.USER
        );

        var testGenre = genreDao.createGenre(
                getRandomAlphabeticalString(20),
                getRandomAlphabeticalString(100)
        );

        testBook1 = bookDao.createBook(
                getRandomNumericString(10),
                getRandomAlphabeticalString(20),
                getRandomAlphabeticalString(100),
                getRandomPositiveNumber(),
                BigDecimal.valueOf(getRandomPositiveNumber()),
                getRandomAlphabeticalString(20) + ".jpg",
                List.of(testGenre)
        );

        testBook2 = bookDao.createBook(
                getRandomNumericString(10),
                getRandomAlphabeticalString(20),
                getRandomAlphabeticalString(100),
                getRandomPositiveNumber(),
                BigDecimal.valueOf(getRandomPositiveNumber()),
                getRandomAlphabeticalString(20) + ".jpg",
                List.of(testGenre)
        );

        testBook3 = bookDao.createBook(
                getRandomNumericString(10),
                getRandomAlphabeticalString(20),
                getRandomAlphabeticalString(100),
                getRandomPositiveNumber(),
                BigDecimal.valueOf(getRandomPositiveNumber()),
                getRandomAlphabeticalString(20) + ".jpg",
                List.of(testGenre)
        );
    }

    @Test
    @DisplayName("Should get all orders by user ID")
    void shouldGetAllOrdersByUserId() {
        // Given
        var testOrder1 = customerOrderService.postNewOrderByUser(testUser.getId(), List.of(
                new CreateUpdateOrderBookDto(testBook1.getId().toString(), 1)
        ));
        var testOrder2 = customerOrderService.postNewOrderByUser(testUser.getId(), List.of(
                new CreateUpdateOrderBookDto(testBook2.getId().toString(), 2)
        ));
        var testOrder3 = customerOrderService.postNewOrderByUser(testUser.getId(), List.of(
                new CreateUpdateOrderBookDto(testBook3.getId().toString(), 3)
        ));

        // When
        var result = customerOrderService.getAllOrdersByUserId(testUser.getId());

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result.stream().map(CustomerOrder::getId).collect(Collectors.toSet()))
                .isEqualTo(Set.of(testOrder1.getId(), testOrder2.getId(), testOrder3.getId()));
    }

    @Test
    @DisplayName("Should get orders list pageable")
    void shouldGetOrdersListPageable() {
        // Given
        customerOrderService.postNewOrderByUser(testUser.getId(), List.of(
                new CreateUpdateOrderBookDto(testBook1.getId().toString(), 1)
        ));
        customerOrderService.postNewOrderByUser(testUser.getId(), List.of(
                new CreateUpdateOrderBookDto(testBook2.getId().toString(), 2)
        ));

        // When
        var result1 = customerOrderService.getOrdersListPageable(0, 1);
        var result2 = customerOrderService.getOrdersListPageable(0, 10);

        // Then
        assertThat(result1).isNotNull();
        assertThat(result1).hasSize(1);
        assertThat(result2).isNotNull();
        assertThat(result2).hasSizeGreaterThan(2);
    }

    @Test
    @DisplayName("Should get order info by ID")
    void shouldGetOrderInfo() {
        // Given
        var testOrder = customerOrderService.postNewOrderByUser(testUser.getId(), List.of(
                new CreateUpdateOrderBookDto(testBook3.getId().toString(), 1)
        ));

        // When
        var result = customerOrderService.getOrderInfo(testOrder.getId());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testOrder.getId());
    }

    @Test
    @DisplayName("Should get order info if belongs to user")
    void shouldGetOrderInfoIfBelongsToUser() {
        // Given
        var testOrder = customerOrderService.postNewOrderByUser(testUser.getId(), List.of(
                new CreateUpdateOrderBookDto(testBook3.getId().toString(), 1)
        ));

        // When
        var result = customerOrderService.getOrderInfoIfBelongsToUser(testOrder.getId(), testUser.getId());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(testOrder.getId());
    }

    @Test
    @DisplayName("Should change order status")
    void shouldChangeOrderStatus() {
        // Given
        var testOrder = customerOrderService.postNewOrderByUser(testUser.getId(), List.of(
                new CreateUpdateOrderBookDto(testBook2.getId().toString(), 1),
                new CreateUpdateOrderBookDto(testBook1.getId().toString(), 3)
        ));

        // When
        customerOrderService.changeOrderStatus(testOrder.getId(), OrderStatus.PROCESSING);

        // Then
        var updatedOrder = customerOrderService.getOrderInfo(testOrder.getId());
        assertThat(updatedOrder).isNotNull();
        assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.PROCESSING);
    }

    @Test
    @DisplayName("Should update order with new items")
    void shouldUpdateOrderWithNewItems() {
        // Given
        var testOrder = customerOrderService.postNewOrderByUser(testUser.getId(), List.of(
                new CreateUpdateOrderBookDto(testBook1.getId().toString(), 1)
        ));

        // When
        var newOrderBooks = List.of(
                new CreateUpdateOrderBookDto(testBook1.getId().toString(), 2),
                new CreateUpdateOrderBookDto(testBook2.getId().toString(), 1),
                new CreateUpdateOrderBookDto(testBook3.getId().toString(), 4)
        );
        var updatedOrder = customerOrderService.updateOrderWithNewItems(testOrder.getId(), newOrderBooks);

        // Then
        assertThat(updatedOrder).isNotNull();

        assertThat(updatedOrder.getOrderBooks()).hasSize(3);
        assertThat(updatedOrder.getOrderBooks().get(0).getQuantity()).isEqualTo(2);
        assertThat(updatedOrder.getOrderBooks().get(0).getId()).isNotNull();
        assertThat(updatedOrder.getOrderBooks().get(0).getBook().getId()).isEqualTo(testBook1.getId());

        assertThat(updatedOrder.getOrderBooks().get(1).getQuantity()).isEqualTo(1);
        assertThat(updatedOrder.getOrderBooks().get(1).getId()).isNotNull();
        assertThat(updatedOrder.getOrderBooks().get(1).getBook().getId()).isEqualTo(testBook2.getId());

        assertThat(updatedOrder.getOrderBooks().get(2).getQuantity()).isEqualTo(4);
        assertThat(updatedOrder.getOrderBooks().get(2).getId()).isNotNull();
        assertThat(updatedOrder.getOrderBooks().get(2).getBook().getId()).isEqualTo(testBook3.getId());
    }
}
