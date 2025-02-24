package com.jdev.customers.service;

import com.jdev.customers.model.CountryDTO;
import com.jdev.customers.model.Customer;
import com.jdev.customers.model.CustomerUpdateDTO;
import com.jdev.customers.repository.CustomerRepository;
import com.jdev.customers.service.client.IRestCountryAPIClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class CustomerService {
    Logger logger = Logger.getLogger(CustomerService.class.getName());

    @Inject
    CustomerRepository customerRepository;

    @RestClient
    IRestCountryAPIClient restCountryAPIClient;


    @Transactional
    public void add(Customer customer) {
        customer.setDemonym(getDemonym(customer.getCountry()));
        customerRepository.persist(customer);
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
    }


    @Transactional
    public void deleteById(long id) {
        customerRepository.deleteById(id);
    }

    private String getDemonym(String countryCode) {
        CountryDTO[] countryDTOs = null;

        try {
            countryDTOs = restCountryAPIClient.getCountry(countryCode);
        } catch (Exception e) {
            logger.warning(String.format("Problem fetching country demonym using code %s", countryCode));
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
