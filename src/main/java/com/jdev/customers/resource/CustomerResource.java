package com.jdev.customers.resource;

import com.jdev.customers.model.Customer;
import com.jdev.customers.model.CustomerUpdateDTO;
import com.jdev.customers.service.CustomerService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    @Inject
    CustomerService customerService;

    @POST
    public Response createCustomer(Customer customer) {
        customerService.add(customer);
        return Response.status(Response.Status.CREATED).entity(customer).build();
    }

    @GET
    public List<Customer> getCustomers(@QueryParam("country") String country) {
        List<Customer> customers;
        if (country != null && !country.isEmpty()) {
            customers = customerService.listByCountry(country);
        } else {
            customers = customerService.listAll();
        }
        return customers;
    }

    @GET
    @Path("/{id}")
    public Response getCustomer(@PathParam("id") long id) {
        Optional<Customer> customer = customerService.findById(id);
        return customer.map(Response::ok)
                .orElse(Response.status(Response.Status.NOT_FOUND))
                .build();
    }

    @PATCH
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") long id, @Valid CustomerUpdateDTO customerUpdateDTO) {
        Optional<Customer> customerOptional = customerService.findById(id);
        if (customerOptional.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        customerService.update(id, customerUpdateDTO);
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") long id) {
        customerService.deleteById(id);
        return Response.status(Response.Status.OK).build();
    }

}
