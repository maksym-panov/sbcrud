package com.maksympanov.hneu.mjt.sbcrud.sbcrud.service;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dao.BookDao;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dao.CustomerOrderDao;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dao.ServiceUserDao;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.dto.order.CreateUpdateOrderBookDto;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.exception.ResourceAccessException;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.CustomerOrder;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.OrderBook;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class CustomerOrderService {

    private CustomerOrderDao customerOrderDao;

    private ServiceUserDao serviceUserDao;

    private BookDao bookDao;

    public List<CustomerOrder> getAllOrdersByUserId(UUID userId) {
        log.info("getAllOrdersByUserId: userId - {}", userId);
        var user = serviceUserDao.getUserByIdThrowable(userId);
        return customerOrderDao.getAllOrdersOfUser(user);
    }

    public List<CustomerOrder> getOrdersListPageable(int pageNumber, int pageSize) {
        log.info("getOrdersListPageable: pageNumber - {}, pageSize - {}", pageNumber, pageSize);
        return customerOrderDao.getOrders(pageNumber, pageSize)
                .stream()
                .toList();
    }

    public CustomerOrder getOrderInfo(UUID orderId) {
        log.info("getOrderInfo: orderId - {}", orderId);
        return customerOrderDao.getOrderByIdThrowable(orderId);
    }

    public CustomerOrder getOrderInfoIfBelongsToUser(UUID orderId, UUID userId) {
        log.info("getOrderInfoIfBelongsToUser: orderId - {}, userId - {}", orderId, userId);
        var order = customerOrderDao.getOrderByIdThrowable(orderId);

        if (!userId.equals(order.getUser().getId())) {
            log.warn("getOrderInfoIfBelongsToUser: user {} does not have order with id {}", userId, orderId);
            throw new ResourceAccessException("User " + userId + " does not have order with id " + order);
        }

        return order;
    }

    public CustomerOrder postNewOrderByUser(UUID userId, List<CreateUpdateOrderBookDto> orderBooks) {
        var user = serviceUserDao.getUserByIdThrowable(userId);

        var orderBooksManaged = orderBooks.stream()
                .map( this::mapBookIdToOrderBook )
                .filter( ob -> Objects.nonNull(ob.getBook()) )
                .toList();

        return customerOrderDao.createOrder(user, orderBooksManaged);
    }

    public void changeOrderStatus(UUID orderId, OrderStatus newStatus) {
        var order = customerOrderDao.getOrderByIdThrowable(orderId);
        customerOrderDao.updateWithStatus(order, newStatus);
    }

    public CustomerOrder updateOrderWithNewItems(UUID orderId, List<CreateUpdateOrderBookDto> newOrderBooks) {
        var order = customerOrderDao.getOrderByIdThrowable(orderId);

        var orderBooksManaged = newOrderBooks.stream()
                .map( this::mapBookIdToOrderBook )
                .filter( ob -> Objects.nonNull(ob.getBook()) )
                .toList();

        return customerOrderDao.updateWithOrderBooks(order, orderBooksManaged);
    }

    private OrderBook mapBookIdToOrderBook(CreateUpdateOrderBookDto ob) {
        var bookId = UUID.fromString(ob.getBookId());
        var book = bookDao.getBookByIdNullable(bookId);
        return OrderBook.builder()
                .book(book)
                .quantity(ob.getQuantity())
                .build();
    }


}
