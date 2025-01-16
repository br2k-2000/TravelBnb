package com.travelbnb.payload;

import lombok.Data;

import java.io.Serializable;

@Data
public class LocationDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private long id;
    private String name;
}
