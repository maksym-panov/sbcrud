package com.maksympanov.hneu.mjt.sbcrud.sbcrud.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.generator.EventType;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "customer_order", schema = "sbc_schema")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerOrder {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ServiceUser user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OrderBook> orderBooks = new HashList<>();

    @Version
    private Integer version;

    @Column(name = "date_created")
    @CreationTimestamp
    private LocalDateTime dateCreated;

    @Column(name = "date_modified")
    @Generated(event = { EventType.INSERT, EventType.UPDATE }, sql = "CURRENT_TIMESTAMP")
    private LocalDateTime dateModified;

}
