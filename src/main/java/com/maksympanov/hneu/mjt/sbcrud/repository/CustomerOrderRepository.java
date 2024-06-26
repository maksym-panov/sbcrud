package com.maksympanov.hneu.mjt.sbcrud.repository;

import com.maksympanov.hneu.mjt.sbcrud.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, UUID> {

    List<CustomerOrder> findAllByUserId(UUID userId);

}
