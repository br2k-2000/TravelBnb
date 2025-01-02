package com.travelbnb.Controller;

import com.travelbnb.Service.CountryService;
import com.travelbnb.payload.CountryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/country")
public class CountryController {


    private CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @PostMapping("/addCountry")
    public ResponseEntity<CountryDto> addCountry(@RequestBody CountryDto countryDto){
        CountryDto countryDto1 = countryService.addCountry(countryDto);

        return new ResponseEntity<>(countryDto1, HttpStatus.CREATED);
    }
    // Retrieve all countries
    //http://localhost:8080/api/v1/country?pageSize=3&pageNo=0&sortBy=emailId&sortDir=desc
    @GetMapping("/getCountries")
    public ResponseEntity<List<CountryDto>> getAllCountries(
            @RequestParam(name="pageSize",defaultValue = "5",required = false)int pageSize,
            @RequestParam(name="pageNo",defaultValue = "0",required = false)int pageNo,
            @RequestParam(name="sortBy",defaultValue = "id",required = false)String sortBy,
            @RequestParam(name="sortDir",defaultValue = "id",required = false)String sortDir

    ){
        List<CountryDto> countries = countryService.getAllCountries(pageSize,pageNo,sortBy,sortDir);
        return new ResponseEntity<>(countries, HttpStatus.OK);
    }

    // Retrieve a single country by id
    //http://localhost:8080/api/v1/country/1
    @GetMapping("/getCountry/{id}")
    public ResponseEntity<CountryDto> getCountryById(@PathVariable long id) {
        CountryDto countryById = countryService.getCountryById(id);
        return new ResponseEntity<>(countryById, HttpStatus.OK);
    }

    // Update country
    @PutMapping("/updateCountry/{id}")
    public ResponseEntity<CountryDto> updateCountry(@PathVariable("id") Long id, @RequestBody CountryDto countryDto) {
        CountryDto updatedCountry = countryService.updateCountry(id, countryDto);
        return new ResponseEntity<>(updatedCountry, HttpStatus.OK);
    }

    // Delete country
    @DeleteMapping("/deleteCountry/{id}")
    public ResponseEntity<String> deleteCountry(@PathVariable("id") Long id) {
        countryService.deleteCountry(id);
        return new ResponseEntity<>("Country deleted successfully", HttpStatus.OK);
    }
}
