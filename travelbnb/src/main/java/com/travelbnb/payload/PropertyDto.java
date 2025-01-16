package com.travelbnb.payload;

import com.travelbnb.Entity.Country;
import com.travelbnb.Entity.Location;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class PropertyDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private Integer noGuests;
    private Integer noBedrooms;
    private Integer noBathrooms;
    private Integer nightlyPrice;
    private Country country;
    private Location location;
    private LocalDateTime dateAdded;
}
