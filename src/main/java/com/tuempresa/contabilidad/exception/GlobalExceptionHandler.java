package com.tuempresa.contabilidad.exception;

import com.tuempresa.contabilidad.dto.ErrorResponse;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {
        int statusCode = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        String errorCode = "INTERNAL_ERROR";
        String message = "Ocurrió un error inesperado.";

        if (exception instanceof jakarta.ws.rs.NotFoundException) {
            statusCode = Response.Status.NOT_FOUND.getStatusCode();
            errorCode = "NOT_FOUND";
            message = "El recurso solicitado no fue encontrado.";
        } else if (exception instanceof jakarta.validation.ValidationException) {
            statusCode = Response.Status.BAD_REQUEST.getStatusCode();
            errorCode = "VALIDATION_ERROR";
            message = "Error de validación.";
        } else if (exception instanceof IllegalArgumentException) {
            statusCode = Response.Status.BAD_REQUEST.getStatusCode();
            errorCode = "ILLEGAL_ARGUMENT";
            message = exception.getMessage();
        }

        ErrorResponse errorResponse = new ErrorResponse(errorCode, message, exception.getMessage());
        return Response.status(statusCode).entity(errorResponse).build();
    }
}
