package com.jdev.customers.exception;

import com.jdev.customers.common.ApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Set;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();

        StringBuilder errors = new StringBuilder("Validation failed for : ");

        for (ConstraintViolation<?> violation : violations) {
            errors.append(violation.getPropertyPath().toString())
                    .append(": ")
                    .append(violation.getMessage())
                    .append("\n");
        }

        ApiResponse response = new ApiResponse("error", null, errors.toString());
        return Response.status(Response.Status.BAD_REQUEST).entity(response).build();
    }
}
