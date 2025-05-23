package com.travelbnb.Service.Impl;

import com.travelbnb.Entity.Country;
import com.travelbnb.Entity.Location;
import com.travelbnb.Entity.Property;
import com.travelbnb.Entity.Room;
import com.travelbnb.Exception.ResourceNotFoundException;
import com.travelbnb.Service.PropertyService;
import com.travelbnb.payload.PropertyDto;
import com.travelbnb.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PropertyServiceImpl implements PropertyService {

    private LocationRepository locationRepository;
    private CountryRepository countryRepository;
    private PropertyRepository propertyRepository;
    private RoomRepository roomRepository;
    private BookingRepository bookingRepository;
    private ImageRepository imageRepository;
    private FavouritesRepository favouritesRepository;

    public PropertyServiceImpl(LocationRepository locationRepository, CountryRepository countryRepository, PropertyRepository propertyRepository, RoomRepository roomRepository, BookingRepository bookingRepository, ImageRepository imageRepository, FavouritesRepository favouritesRepository) {
        this.locationRepository = locationRepository;
        this.countryRepository = countryRepository;
        this.propertyRepository = propertyRepository;
        this.roomRepository = roomRepository;
        this.bookingRepository = bookingRepository;
        this.imageRepository = imageRepository;
        this.favouritesRepository = favouritesRepository;
    }

    @Override
    public PropertyDto addProperty(long locationId, long countryId, PropertyDto propertyDto) {
        Optional<Location> byId = locationRepository.findById(locationId);
        if (byId.isPresent()) {
            Location location = byId.get();
            propertyDto.setLocation(location);
        } else {
            throw new ResourceNotFoundException("Location not found with id: " + locationId);
        }
        Optional<Country> byId1 = countryRepository.findById(countryId);
        if (byId1.isPresent()){
            Country country = byId1.get();
            propertyDto.setCountry(country);
        }else {
            throw new ResourceNotFoundException("Country not found with id: "+countryId);
        }
        Property property = dtoToEntity(propertyDto);

        Optional<Property> existProperty = propertyRepository.findByNameAndLocationAndCountry(property.getName(), property.getLocation(), property.getCountry());
        if (existProperty.isPresent()){
            //Property Already exist
            throw  new ResourceNotFoundException("Property already exists with name: " + property.getName());
        }
        Property save = propertyRepository.save(property);
        return entityToDto(save);


    }

    @Transactional
    @Override
    @CacheEvict(value = "properties", key = "#propertyId")
    public void deleteProperty(Long propertyId) {
        // Check if the property exists
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property with ID " + propertyId + " does not exist."));

        // Step 1: Delete favourites
        favouritesRepository.deleteByProperty(property);

        // Get all rooms associated with the property
        List<Room> rooms = roomRepository.findByProperty(property);

        // Delete bookings associated with each room
        for (Room room : rooms) {
            bookingRepository.deleteByRoom(room);
        }

        // Delete rooms associated with the property
        roomRepository.deleteByProperty(property);

        // Delete images associated with the property
        imageRepository.deleteByProperty(property);

        // Finally, delete the property
        propertyRepository.delete(property);
    }

    @Override
    @Cacheable(value = "properties", key = "#propertyId")
    public PropertyDto getPropertyById(long propertyId) {
        Optional<Property> byId = propertyRepository.findById(propertyId);
        if (byId.isPresent()){
            Property property = byId.get();
            PropertyDto propertyDto = entityToDto(property);
            return propertyDto;
        }else {
            throw new ResourceNotFoundException("Property not found with id: "+propertyId);
        }
    }


    @Override
    public List<PropertyDto> getAllProperty(int pageSize, int pageNo, String sortBy, String sortDir) {
        PageRequest pageable=null;
        if (sortDir.equalsIgnoreCase("asc")){
            pageable= PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        }else if (sortDir.equalsIgnoreCase("desc")){
            pageable= PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }else {
            pageable=PageRequest.of(pageNo,pageSize);
        }
        Page<Property> all = propertyRepository.findAll(pageable);
        List<Property> content = all.getContent();
        List<PropertyDto> collect = content.stream().map(e -> entityToDto(e)).collect(Collectors.toList());
        return collect;

    }

    @Override
    public PropertyDto updateProperty(long propertyId, long locationId, long countryId, PropertyDto propertyDto) {
        Property property=null;
        property = propertyRepository.findById(propertyId).orElseThrow(
                ()->new ResourceNotFoundException("Property not found with id: "+propertyId)
        );

        Location location = locationRepository.findById(locationId).orElseThrow(
                ()->new ResourceNotFoundException("Location not found with id: "+locationId)
        );
        propertyDto.setLocation(location);

        Country country = countryRepository.findById(countryId).orElseThrow(
                () -> new ResourceNotFoundException("Country not found with id: " + countryId)
        );
        propertyDto.setCountry(country);
        propertyDto.setDateAdded(property.getDateAdded());
        //System.out.println(propertyDto);

        property=dtoToEntity(propertyDto);
        property.setId(propertyId);

        Property updatedProperty = propertyRepository.save(property);
        PropertyDto updatedPropertyDto  = entityToDto(updatedProperty);
        return updatedPropertyDto;
    }

    @Override
    public List<PropertyDto> searchProperty(String name) {
        List<Property> properties = propertyRepository.searchProperty(name);
        List<PropertyDto> collect = properties.stream().map(e -> entityToDto(e)).collect(Collectors.toList());
        return collect;
    }


    //dto to entity
    Property dtoToEntity(PropertyDto propertyDto){
        Property property=new Property();
        property.setName(propertyDto.getName());
        property.setNoGuests(propertyDto.getNoGuests());
        property.setNightlyPrice(propertyDto.getNightlyPrice());
        property.setNoBedrooms(propertyDto.getNoBedrooms());
        property.setNoBathrooms(propertyDto.getNoBathrooms());
        property.setLocation(propertyDto.getLocation());
        property.setCountry(propertyDto.getCountry());
        property.setDateAdded(propertyDto.getDateAdded());
        return property;
    }
    //entity to dto
    PropertyDto entityToDto(Property property){
        PropertyDto dto=new PropertyDto();
        dto.setId(property.getId());
        dto.setName(property.getName());
        dto.setNoBedrooms(property.getNoBedrooms());
        dto.setNoBathrooms(property.getNoBathrooms());
        dto.setNightlyPrice(property.getNightlyPrice());
        dto.setNoGuests(property.getNoGuests());
        dto.setCountry(property.getCountry());
        dto.setLocation(property.getLocation());
        dto.setDateAdded(property.getDateAdded());
        return dto;
    }


    }
