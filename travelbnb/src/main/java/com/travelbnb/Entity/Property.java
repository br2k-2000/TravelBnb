package com.travelbnb.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "property")
@JsonSerialize
public class Property implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "no_guests", nullable = false)
    private Integer noGuests;

    @Column(name = "no_bathrooms", nullable = false)
    private Integer noBathrooms;

    @Column(name = "no_bedrooms", nullable = false)
    private Integer noBedrooms;

    @Column(name = "nightly_price", nullable = false)
    private Integer nightlyPrice;

    @ManyToOne
    @JoinColumn(name = "country_id")
    @JsonBackReference("property-country")
    private Country country;

    @ManyToOne
    @JoinColumn(name = "location_id")
    @JsonBackReference("property-location")
    private Location location;

    @Column(name = "date_added", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateAdded;  // it will track when the property was added

    @PrePersist
    protected void onCreate(){
        if (dateAdded==null){
            dateAdded= LocalDateTime.now();
        }
    }
}