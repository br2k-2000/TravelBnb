package com.travelbnb.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "room_number", nullable = false, unique = true)
    private Integer roomNumber;

    @Column(name = "status")
    private Boolean status;   // false means available, true means booked

    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    @Version
    private Integer version;

    public boolean isStatus() {
        return status;
    }
}