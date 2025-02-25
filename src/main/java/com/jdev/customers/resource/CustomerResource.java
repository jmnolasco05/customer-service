package com.jdev.customers.resource;

import com.jdev.customers.common.ApiResponse;
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
    public Response createCustomer(@Valid Customer customer) {
        customerService.add(customer);
        ApiResponse response = new ApiResponse("success", customer, "Customer created successfully");
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    public Response getCustomers(@QueryParam("country") String country) {
        List<Customer> customers;
        if (country != null && !country.isEmpty()) {
            customers = customerService.listByCountry(country);
        } else {
            customers = customerService.listAll();
        }

        ApiResponse response;
        if (customers.isEmpty()) {
            response = new ApiResponse("error", null, "No customers found");
            return Response.status(Response.Status.NOT_FOUND).entity(response).build();
        } else {
            response = new ApiResponse("success", customers, "Customer retrieved successfully");
            return Response.status(Response.Status.OK).entity(response).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getCustomer(@PathParam("id") long id) {
        Optional<Customer> customer = customerService.findById(id);
        if(customer.isEmpty()) {
            throw new NotFoundException(String.format("No Customer found with id[%s]", id));
        }

        ApiResponse response = new ApiResponse("success", customer, "Customer retrieved successfully");
        return Response.status(Response.Status.OK).entity(response).build();

    }

    @PATCH
    @Path("/{id}")
    public Response updateCustomer(@PathParam("id") long id, @Valid CustomerUpdateDTO customerUpdateDTO) {
        Optional<Customer> customerOptional = customerService.findById(id);
        if (customerOptional.isEmpty()) {
            throw new NotFoundException(String.format("No Customer found with id[%s]", id));
        }

        customerService.update(id, customerUpdateDTO);
        ApiResponse response = new ApiResponse("success", null, "Customer updated successfully");
        return Response.status(Response.Status.OK).entity(response).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteCustomer(@PathParam("id") long id) {
        boolean deleted = customerService.deleteById(id);
        if(!deleted) {
            throw new NotFoundException(String.format("No Customer found with id[%s]", id));
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
