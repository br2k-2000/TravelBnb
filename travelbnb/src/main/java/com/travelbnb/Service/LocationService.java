package com.travelbnb.Service;

import com.travelbnb.payload.LocationDto;

import java.util.List;

public interface LocationService {

    LocationDto addLocation(LocationDto locationDto);

    void deleteLocation(long id);

    LocationDto getLocationById(long locationId);

    List<LocationDto> getAllLocation(int pageSize, int pageNo, String sortBy, String sortDir);

    LocationDto updateLocation(long locationId, LocationDto locationDto);
}
