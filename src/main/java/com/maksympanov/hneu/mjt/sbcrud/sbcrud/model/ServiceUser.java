package com.maksympanov.hneu.mjt.sbcrud.sbcrud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.maksympanov.hneu.mjt.sbcrud.sbcrud.auth.CustomGrantedAuthority;
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
import java.util.*;

@Entity
@Table(name = "service_user", schema = "sbc_schema")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceUser {

    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    @Column(nullable = false)
    private String email;

    @Column(name = "phone_number", length = 30)
    private String phoneNumber;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Favourite> favourites = new ArrayList<>();

    @Version
    private Integer version;

    @Column(name = "date_created")
    @CreationTimestamp
    private LocalDateTime dateCreated;

    @Column(name = "date_modified")
    @Generated(event = { EventType.INSERT, EventType.UPDATE }, sql = "CURRENT_TIMESTAMP")
    private LocalDateTime dateModified;

    @JsonIgnore
    public List<CustomGrantedAuthority> getAuthorities() {
        if (role == UserRole.USER || role == UserRole.VENDOR) {
            return List.of(new CustomGrantedAuthority(role));
        }
        return Arrays.stream(UserRole.values())
                .map( CustomGrantedAuthority::new )
                .toList();
    }

}
