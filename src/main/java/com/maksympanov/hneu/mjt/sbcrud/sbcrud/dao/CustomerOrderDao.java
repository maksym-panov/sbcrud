package com.maksympanov.hneu.mjt.sbcrud.sbcrud.dao;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.exception.NotFoundException;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.CustomerOrder;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.OrderBook;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.OrderStatus;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.ServiceUser;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.repository.CustomerOrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomerOrderDao {

    private CustomerOrderRepository customerOrderRepository;

    public CustomerOrder createOrder(ServiceUser actorUser, List<OrderBook> orderBooks) {
        return customerOrderRepository.save(
                CustomerOrder.builder()
                        .user(actorUser)
                        .orderBooks(orderBooks)
                        .status(OrderStatus.CREATED)
                        .build()
        );
    }

    public CustomerOrder getOrderByIdThrowable(UUID id) {
        return customerOrderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not find CustomerOrder with id: " + id));
    }

    public CustomerOrder getOrderByIdNullable(UUID id) {
        return customerOrderRepository.findById(id).orElse(null);
    }

    public List<CustomerOrder> getAllOrdersOfUser(ServiceUser user) {
        return customerOrderRepository.findAllByUserId(user.getId());
    }

    public Page<CustomerOrder> getOrders(int pageNumber, int pageSize) {
        var request = PageRequest.of(pageNumber, pageSize, Sort.by("date_created"));
        return customerOrderRepository.findAll(request);
    }

    public CustomerOrder updateWithStatus(CustomerOrder order, OrderStatus status) {
        order.setStatus(status);
        return customerOrderRepository.save(order);
    }

    public CustomerOrder updateWithOrderBooks(CustomerOrder order, List<OrderBook> orderBooks) {
        order.setOrderBooks(orderBooks);
        return customerOrderRepository.save(order);
    }

}
