package com.travelbnb.Service.Impl;

import com.travelbnb.Entity.Country;
import com.travelbnb.Exception.CountryAlreadyExistsException;
import com.travelbnb.Exception.ResourceNotFoundException;
import com.travelbnb.Service.CountryService;
import com.travelbnb.payload.CountryDto;
import com.travelbnb.repository.CountryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CountryServiceImpl implements CountryService {
    
    private CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public CountryDto addCountry(CountryDto countryDto) {
        // Check if the country name already exists
        if (countryRepository.existsByName(countryDto.getName())) {
            throw new CountryAlreadyExistsException("Country with name '" + countryDto.getName() + "' already exists");
        }
        Country country = DtoToEntity(countryDto);

        Country savedCountry = countryRepository.save(country);
        return EntityToDto(savedCountry);
    }



    @Override
    public List<CountryDto> getAllCountries(int pageSize, int pageNo, String sortBy, String sortDir) {
        PageRequest pageable = null;
        if (sortDir.equalsIgnoreCase("desc")){
            pageable= PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        } else if (sortDir.equalsIgnoreCase("asc")) {
            pageable=PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        }
        Page<Country> all = countryRepository.findAll(pageable);
        List<Country> countries = all.getContent();
        return countries.stream()
                .map(this::EntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CountryDto getCountryById(Long id) {
        Optional<Country> byId = countryRepository.findById(id);
        Country country;
        if (byId.isPresent()) {
            country = byId.get();
        } else {
            throw new ResourceNotFoundException("country not found with id:" + id);
        }
        return EntityToDto(country);
    }

    @Override
    public CountryDto updateCountry(Long id, CountryDto countryDto) {
        Optional<Country> byId = countryRepository.findById(id);
        Country existingCountry;
        if (byId.isPresent()) {
            existingCountry = byId.get();
        } else {
            throw new ResourceNotFoundException("country not found with id: " + id);
        }
        existingCountry.setName(countryDto.getName());
        Country updatedCountry = countryRepository.save(existingCountry);
        return EntityToDto(updatedCountry);
    }

    @Override
    public void deleteCountry(Long id) {
    countryRepository.deleteById(id);

    }

    private CountryDto EntityToDto(Country savedCountry) {
        CountryDto countryDto = new CountryDto();
        countryDto.setName(savedCountry.getName());
        countryDto.setId(savedCountry.getId());
        return countryDto;
    }

    private Country DtoToEntity(CountryDto countryDto) {
        Country country = new Country();
        country.setName(countryDto.getName());
        country.setId(countryDto.getId());
        return country;
    }
}
