package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.PeriodoContableBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PeriodoContable;

/**
 * REST Resource for accounting periods (Períodos Contables).
 */
@Path("periodos-contables")
public class PeriodoContableResource implements Serializable {
    
    @Inject
    PeriodoContableBean periodoContableBean;
    
    /**
     * Get all accounting periods.
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response findAll() {
        try {
            List<PeriodoContable> periodos = periodoContableBean.findAll();
            return Response.ok(periodos).build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Get period by ID.
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findById(@PathParam("id") UUID id) {
        try {
            PeriodoContable periodo = periodoContableBean.findById(id);
            if (periodo == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(periodo).build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Get current active period.
     */
    @GET
    @Path("activo")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findPeriodoActivo() {
        try {
            PeriodoContable periodo = periodoContableBean.findPeriodoActivo();
            if (periodo == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse("No hay período contable activo"))
                    .build();
            }
            return Response.ok(periodo).build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Create a new accounting period.
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response crearPeriodo(PeriodoContable periodo, @Context UriInfo uriInfo) {
        try {
            UUID id = UUID.randomUUID();
            periodo.setIdPeriodoContable(id);
            if (periodo.getActivo() == null) {
                periodo.setActivo(true);
            }
            if (periodo.getCerrado() == null) {
                periodo.setCerrado(false);
            }
            periodoContableBean.persistEntity(periodo);
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(id.toString());
            return Response.created(uriBuilder.build()).build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Close an accounting period.
     */
    @PUT
    @Path("{id}/cerrar")
    @Produces({MediaType.APPLICATION_JSON})
    public Response cerrarPeriodo(@PathParam("id") UUID id) {
        try {
            PeriodoContable periodo = periodoContableBean.findById(id);
            if (periodo == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            periodo.setCerrado(true);
            periodo.setFechaCierre(new Date());
            periodoContableBean.updateEntity(periodo);
            return Response.ok().build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Activate/deactivate an accounting period.
     */
    @PUT
    @Path("{id}/activar/{activo}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response activarPeriodo(@PathParam("id") UUID id, @PathParam("activo") Boolean activo) {
        try {
            PeriodoContable periodo = periodoContableBean.findById(id);
            if (periodo == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            periodo.setActivo(activo);
            periodoContableBean.updateEntity(periodo);
            return Response.ok().build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Error response class
     */
    public static class ErrorResponse {
        private String message;
        
        public ErrorResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
