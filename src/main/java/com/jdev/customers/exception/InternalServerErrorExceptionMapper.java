package com.jdev.customers.exception;

import com.jdev.customers.common.ApiResponse;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InternalServerErrorExceptionMapper implements ExceptionMapper<InternalServerErrorException> {

    @Override
    public Response toResponse(InternalServerErrorException e) {
        ApiResponse response = new ApiResponse("error", null, e.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(response).build();
    }
}
