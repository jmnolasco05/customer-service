package com.jdev.customers.resource;

import com.jdev.customers.common.ApiResponse;
import com.jdev.customers.model.Customer;
import com.jdev.customers.model.CustomerUpdateDTO;
import com.jdev.customers.service.CustomerService;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerResourceTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerResource customerResource;

    private Customer customer;
    private CustomerUpdateDTO customerUpdateDTO;

    @BeforeEach
    public void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Juan");
        customer.setMiddleName("Miguel");
        customer.setLastName("Ruiz");
        customer.setSecondLastName("Perez");
        customer.setEmail("test@example.com");
        customer.setPhone("123456789");
        customer.setAddress("Test Address");
        customer.setCountry("Test Country");
        customer.setDemonym("Demonym");

        customerUpdateDTO = new CustomerUpdateDTO("newEmail@example.com", "newPhone", "newAddress", "newCountry");
    }

    @Test
    public void testCreateCustomer() {
        Response response = customerResource.createCustomer(customer);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        ApiResponse apiResponse = (ApiResponse) response.getEntity();
        assertEquals("success", apiResponse.getStatus());
        assertNotNull(apiResponse.getData());
        assertEquals("Customer created successfully", apiResponse.getMessage());
    }

    @Test
    public void testGetAllCustomers() {
        when(customerService.listAll()).thenReturn(Collections.singletonList(customer));

        Response response = customerResource.getCustomers(null);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        ApiResponse apiResponse = (ApiResponse) response.getEntity();
        assertEquals("success", apiResponse.getStatus());
        assertNotNull(apiResponse.getData());
        assertEquals("Customer retrieved successfully", apiResponse.getMessage());
        verify(customerService, times(1)).listAll();
        verify(customerService, times(0)).listByCountry(anyString());

    }

    @Test
    public void testGetAllCustomersWhenCountryEmpty() {
        when(customerService.listAll()).thenReturn(Collections.singletonList(customer));

        Response response = customerResource.getCustomers("");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        ApiResponse apiResponse = (ApiResponse) response.getEntity();
        assertEquals("success", apiResponse.getStatus());
        assertNotNull(apiResponse.getData());
        assertEquals("Customer retrieved successfully", apiResponse.getMessage());
        verify(customerService, times(1)).listAll();
        verify(customerService, times(0)).listByCountry(anyString());
    }

    @Test
    public void testGetAllCustomersWhenNotFound() {
        when(customerService.listAll()).thenReturn(Collections.emptyList());

        Response response = customerResource.getCustomers(null);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        ApiResponse apiResponse = (ApiResponse) response.getEntity();
        assertEquals("error", apiResponse.getStatus());
        assertNull(apiResponse.getData());
        assertEquals("No customers found", apiResponse.getMessage());
        verify(customerService, times(1)).listAll();
        verify(customerService, times(0)).listByCountry(anyString());
    }

    @Test
    public void testGetCustomersByCountry() {
        when(customerService.listByCountry(anyString())).thenReturn(Collections.singletonList(customer));

        Response response = customerResource.getCustomers("DO");

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        ApiResponse apiResponse = (ApiResponse) response.getEntity();
        assertEquals("success", apiResponse.getStatus());
        assertNotNull(apiResponse.getData());
        assertEquals("Customer retrieved successfully", apiResponse.getMessage());
        verify(customerService, times(0)).listAll();
        verify(customerService, times(1)).listByCountry(anyString());

    }

    @Test
    public void testGetCustomersByCountryWhenNotFound() {
        when(customerService.listByCountry(anyString())).thenReturn(Collections.emptyList());

        Response response = customerResource.getCustomers("US");

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        ApiResponse apiResponse = (ApiResponse) response.getEntity();
        assertEquals("error", apiResponse.getStatus());
        assertNull(apiResponse.getData());
        assertEquals("No customers found", apiResponse.getMessage());
        verify(customerService, times(0)).listAll();
        verify(customerService, times(1)).listByCountry(anyString());
    }

    @Test
    public void testGetCustomer() {
        when(customerService.findById(1L)).thenReturn(Optional.of(customer));

        Response response = customerResource.getCustomer(1L);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        ApiResponse apiResponse = (ApiResponse) response.getEntity();
        assertEquals("success", apiResponse.getStatus());
        assertNotNull(apiResponse.getData());
    }

    @Test
    public void testGetCustomerWhenNotFound() {
        when(customerService.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerResource.getCustomer(1L));
    }

    @Test
    public void testUpdateCustomer() {
        when(customerService.findById(1L)).thenReturn(Optional.of(customer));
        doNothing().when(customerService).update(1L, customerUpdateDTO);

        Response response = customerResource.updateCustomer(1L, customerUpdateDTO);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        ApiResponse apiResponse = (ApiResponse) response.getEntity();
        assertEquals("success", apiResponse.getStatus());
        assertEquals("Customer updated successfully", apiResponse.getMessage());
    }

    @Test
    public void testUpdateCustomerWhenNotFound() {
        when(customerService.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerResource.updateCustomer(1L, customerUpdateDTO));
    }

    @Test
    public void testDeleteCustomer() {
        when(customerService.deleteById(1L)).thenReturn(true);

        Response response = customerResource.deleteCustomer(1L);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }

    @Test
    public void testDeleteCustomerWhenNotFound() {
        when(customerService.deleteById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> customerResource.deleteCustomer(1L));
    }
}
