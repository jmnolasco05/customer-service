package com.jdev.customers.exception;

import com.jdev.customers.common.ApiResponse;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException e) {
        ApiResponse response = new ApiResponse("error", null, e.getMessage());
        return Response.status(Response.Status.NOT_FOUND).entity(response).build();
    }
}
