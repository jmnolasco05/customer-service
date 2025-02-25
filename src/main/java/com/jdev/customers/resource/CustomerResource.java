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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.util.List;
import java.util.Optional;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    @Inject
    CustomerService customerService;

    @POST
    @Operation(
            summary = "Create a new customer",
            description = "Add new customer in the database."
    )
    @APIResponse(responseCode = "201", description = "Customer created successfully")
    @APIResponse(responseCode = "400", description = "Invalid customer data")
    public Response createCustomer(@Valid Customer customer) {
        customerService.add(customer);
        ApiResponse response = new ApiResponse("success", customer, "Customer created successfully");
        return Response.status(Response.Status.CREATED).entity(response).build();
    }

    @GET
    @Operation(
            summary = "Create a new customer",
            description = "Add new customer in the database."
    )
    @APIResponse(responseCode = "201", description = "Customers retrieved successfully")
    @APIResponse(responseCode = "404", description = "No customers found")
    public Response getCustomers(
            @Parameter(description = "Country to filter customers")
            @QueryParam("country") String country) {
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
    @Operation(
            summary = "Create a new customer",
            description = "Add new customer in the database."
    )
    @APIResponse(responseCode = "201", description = "Customer retrieved successfully")
    @APIResponse(responseCode = "404", description = "Customer not found")
    public Response getCustomer(
            @Parameter(description = "ID of the customer to retrieve", required = true)
            @PathParam("id") long id) {
        Optional<Customer> customer = customerService.findById(id);
        if(customer.isEmpty()) {
            throw new NotFoundException(String.format("No Customer found with id[%s]", id));
        }

        ApiResponse response = new ApiResponse("success", customer, "Customer retrieved successfully");
        return Response.status(Response.Status.OK).entity(response).build();

    }

    @PATCH
    @Path("/{id}")
    @Operation(
            summary = "Update customer by ID",
            description = "Updates an existing customer's information."
    )
    @APIResponse(responseCode = "200", description = "Customer updated successfully")
    @APIResponse(responseCode = "404", description = "Customer not found")
    @APIResponse(responseCode = "400", description = "Invalid update data")
    public Response updateCustomer(
            @Parameter(description = "ID of the customer to update", required = true)
            @PathParam("id") long id, @Valid CustomerUpdateDTO customerUpdateDTO) {
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
    @Operation(
            summary = "Delete customer by ID",
            description = "Deletes a customer based on their unique ID."
    )
    @APIResponse(responseCode = "204", description = "Customer deleted successfully")
    @APIResponse(responseCode = "404", description = "Customer not found")
    public Response deleteCustomer(
            @Parameter(description = "ID of the customer to delete", required = true)
            @PathParam("id") long id) {
        boolean deleted = customerService.deleteById(id);
        if(!deleted) {
            throw new NotFoundException(String.format("No Customer found with id[%s]", id));
        }

        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
