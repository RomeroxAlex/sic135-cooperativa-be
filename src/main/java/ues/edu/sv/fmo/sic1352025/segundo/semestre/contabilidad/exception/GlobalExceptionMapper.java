package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.ErrorResponseDto;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        LOGGER.log(Level.SEVERE, "Error procesando solicitud", exception);

        if (exception instanceof ResourceNotFoundException) {
            return buildResponse(Response.Status.NOT_FOUND, "NOT_FOUND", 
                exception.getMessage(), null);
        }

        if (exception instanceof BusinessException businessEx) {
            return buildResponse(Response.Status.BAD_REQUEST, businessEx.getCode(), 
                businessEx.getMessage(), null);
        }

        if (exception instanceof ConstraintViolationException constraintEx) {
            String details = constraintEx.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
            return buildResponse(Response.Status.BAD_REQUEST, "VALIDATION_ERROR", 
                "Error de validación", details);
        }

        if (exception instanceof IllegalArgumentException) {
            return buildResponse(Response.Status.BAD_REQUEST, "BAD_REQUEST", 
                exception.getMessage(), null);
        }

        return buildResponse(Response.Status.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", 
            "Error interno del servidor", exception.getMessage());
    }

    private Response buildResponse(Response.Status status, String code, String message, String details) {
        ErrorResponseDto error = new ErrorResponseDto(code, message, details);
        return Response.status(status).entity(error).build();
    }
}
