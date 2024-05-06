package com.maksympanov.hneu.mjt.sbcrud.sbcrud.repository;

import com.maksympanov.hneu.mjt.sbcrud.sbcrud.model.CustomerOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerOrderRepository extends CrudRepository<CustomerOrder, UUID> {
}
