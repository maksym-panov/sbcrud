package com.maksympanov.hneu.mjt.sbcrud.dao;

import com.maksympanov.hneu.mjt.sbcrud.exception.NotFoundException;
import com.maksympanov.hneu.mjt.sbcrud.model.CustomerOrder;
import com.maksympanov.hneu.mjt.sbcrud.model.OrderBook;
import com.maksympanov.hneu.mjt.sbcrud.model.OrderStatus;
import com.maksympanov.hneu.mjt.sbcrud.model.ServiceUser;
import com.maksympanov.hneu.mjt.sbcrud.repository.CustomerOrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CustomerOrderDao {

    private CustomerOrderRepository customerOrderRepository;

    public CustomerOrder createOrder(ServiceUser actorUser, List<OrderBook> orderBooks) {
        var order = CustomerOrder.builder()
                .user(actorUser)
                .status(OrderStatus.CREATED)
                .build();
        order.bindAllOrderBooks(orderBooks);
        return customerOrderRepository.save(order);
    }

    public CustomerOrder getOrderByIdThrowable(UUID id) {
        return customerOrderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not find CustomerOrder with id: " + id));
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
