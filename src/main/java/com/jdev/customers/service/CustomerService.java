package com.jdev.customers.service;

import com.jdev.customers.model.Customer;
import com.jdev.customers.repository.CustomerRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CustomerService {

    @Inject
    CustomerRepository customerRepository;

    @Transactional
    public void add(Customer customer) {
        customer.setDemonym(getDemonym(customer.getCountry()));
        customerRepository.persist(customer);
    }

    public List<Customer> listAll() {
        return  customerRepository.listAll();
    }

    public List<Customer> listByCountry(String country) {
        return customerRepository.find("country", country).list();
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findByIdOptional(id);
    }

    @Transactional
    public void update(long id, Customer customer) {
        Optional<Customer> customerOptional = customerRepository.findByIdOptional(id);

        if (customerOptional.isEmpty()) {
            throw new NotFoundException(String.format("Customer with id %s not found", id));
        }

        Customer customerToUpdate = customerOptional.get();
        customerToUpdate.setEmail(customer.getEmail());
        customerToUpdate.setAddress(customer.getAddress());
        customerToUpdate.setPhone(customer.getPhone());
        customerToUpdate.setCountry(customer.getCountry());
        customerToUpdate.setDemonym(getDemonym(customer.getDemonym()));
        customerRepository.persist(customerToUpdate);
    }


    @Transactional
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    private String getDemonym(String country) {
        System.out.println(country);
        return "Dominican";
    }



}
