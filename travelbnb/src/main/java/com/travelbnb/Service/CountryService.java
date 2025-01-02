package com.travelbnb.Service;

import com.travelbnb.Entity.Country;
import com.travelbnb.payload.CountryDto;

import java.util.List;

public interface CountryService {

    CountryDto addCountry(CountryDto countryDto);


    List<CountryDto> getAllCountries(int pageSize, int pageNo, String sortBy, String sortDir);

    CountryDto getCountryById(Long id);

    CountryDto updateCountry(Long id, CountryDto countryDto);

    void deleteCountry(Long id);
}
