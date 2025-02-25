package com.jdev.customers.service;

import com.jdev.customers.model.CountryDTO;
import com.jdev.customers.model.Customer;
import com.jdev.customers.model.CustomerUpdateDTO;
import com.jdev.customers.repository.CustomerRepository;
import com.jdev.customers.service.client.IRestCountryAPIClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class CustomerService {

    @Inject
    CustomerRepository customerRepository;

    @RestClient
    IRestCountryAPIClient restCountryAPIClient;


    @Transactional
    public void add(Customer customer) {
        try {
            customer.setDemonym(getDemonym(customer.getCountry()));
            customerRepository.persist(customer);
        } catch (InternalServerErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException("Error adding new customer", e);
        }
    }

    public List<Customer> listAll() {
        return  customerRepository.listAll();
    }

    public List<Customer> listByCountry(String country) {
        return customerRepository.find("LOWER(country) = LOWER(?1)", country).list();
    }

    public Optional<Customer> findById(long id) {
        return customerRepository.findByIdOptional(id);
    }

    @Transactional
    public void update(long id, CustomerUpdateDTO customerUpdateDTO) {

        try {

            Optional<Customer> customerOptional = customerRepository.findByIdOptional(id);
            if (customerOptional.isEmpty()) {
                throw new NotFoundException(String.format("No Customer found with id[%s]", id));
            }

            Customer customer = customerOptional.get();
            customer.setEmail(customerUpdateDTO.email());
            customer.setAddress(customerUpdateDTO.address());
            customer.setPhone(customerUpdateDTO.phone());
            customer.setCountry(customerUpdateDTO.country());
            customer.setDemonym(getDemonym(customerUpdateDTO.country()));
            customerRepository.persist(customer);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerErrorException("Error updating Customer", e);
        }
    }


    @Transactional
    public boolean deleteById(long id) {
        try {
            return customerRepository.deleteById(id);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error deleting Customer", e);
        }
    }

    private String getDemonym(String countryCode) {
        CountryDTO[] countryDTOs;

        try {
            countryDTOs = restCountryAPIClient.getCountry(countryCode);
        } catch (Exception e) {
            throw new InternalServerErrorException(String.format("Could not get country by code[%s]", countryCode));
        }

        return Optional.ofNullable(countryDTOs)
                .filter(countries -> countries.length > 0)
                .map(countries -> countries[0])
                .map(CountryDTO::demonyms)
                .map(CountryDTO.Demonyms::eng)
                .map(eng -> eng.get("m"))
                .orElse(null);
    }



}
