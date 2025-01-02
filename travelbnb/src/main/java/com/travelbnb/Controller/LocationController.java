package com.travelbnb.Controller;

import com.travelbnb.Service.LocationService;
import com.travelbnb.payload.LocationDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/location")
public class LocationController {

        private LocationService locationService;

        public LocationController(LocationService locationService) {
            this.locationService = locationService;
        }
        //http://localhost:8080/api/v1/location/addLocation
        @PostMapping("/addLocation")
        public ResponseEntity<LocationDto> addLocation(@RequestBody LocationDto locationDto){
            LocationDto savedLocation = locationService.addLocation(locationDto);
            return new ResponseEntity<>(savedLocation, HttpStatus.CREATED);
        }
        // http://localhost:8080/api/v1/location/deleteLocation?id=5
        @DeleteMapping("/deleteLocation")
        public ResponseEntity<String> deleteLocation(@RequestParam long id){
            locationService.deleteLocation(id);
            return new ResponseEntity<>("Record is deleted",HttpStatus.OK);
        }
       // http://localhost:8080/api/v1/location/ById?locationId=11
        @GetMapping("/ById")
        public ResponseEntity<LocationDto> getLocationById(@RequestParam long locationId){
            LocationDto locationById = locationService.getLocationById(locationId);
            return new ResponseEntity<>(locationById,HttpStatus.OK);
        }
        // http://localhost:8080/api/v1/location?pageSize=2&pageNo=2&sortBy=name&sortDir=desc
        @GetMapping
        public ResponseEntity<List<LocationDto>> getAllLocation(
                @RequestParam(name="pageSize",defaultValue = "5",required = false) int pageSize,
                @RequestParam(name="pageNo",defaultValue = "0",required = false) int pageNo,
                @RequestParam(name="sortBy",defaultValue = "id",required = false) String sortBy,
                @RequestParam(name="sortDir",defaultValue = "id",required = false) String sortDir
        ){
            List<LocationDto> allLocation = locationService.getAllLocation(pageSize, pageNo, sortBy, sortDir);
            return new ResponseEntity<>(allLocation,HttpStatus.OK);
        }
        //http://localhost:8080/api/v1/location/8
        @PutMapping("{locationId}")
        public ResponseEntity<LocationDto> updateLocation(@PathVariable long locationId,
                                                          @RequestBody LocationDto  locationDto){
            LocationDto dto = locationService.updateLocation(locationId, locationDto);
            return new ResponseEntity<>(dto,HttpStatus.OK);
        }
    }


