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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "book", schema = "sbc_schema")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    @Column(length = 31, unique = true, nullable = false)
    private String isbn;

    @Column(name = "book_name")
    private String bookName;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantity;

    private String description;

    @Column(name = "image_uri")
    private String imageUri;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_genre",
            schema = "sbc_schema",
            joinColumns = { @JoinColumn(name = "book_id") },
            inverseJoinColumns = { @JoinColumn(name = "genre_id") }
    )
    private Set<Genre> genres = new HashSet<>();

    @Version
    private Integer version;

    @Column(name = "date_created")
    @CreationTimestamp
    private LocalDateTime dateCreated;

    @Column(name = "date_modified")
    @Generated(event = { EventType.INSERT, EventType.UPDATE }, sql = "CURRENT_TIMESTAMP")
    private LocalDateTime dateModified;

}
