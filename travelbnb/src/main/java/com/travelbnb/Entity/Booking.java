package com.travelbnb.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "mobile", nullable = false, unique = true, length = 10)
    private String mobile;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    @Column(name = "price")
    private Integer price;

    @Column(name = "total_nights", nullable = false)
    private Integer totalNights;

    @Version
    private Integer version;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

}