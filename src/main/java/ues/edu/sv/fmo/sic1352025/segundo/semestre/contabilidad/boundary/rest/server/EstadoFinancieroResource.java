package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.inject.Inject;
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
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.EstadoFinanciero;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ContabilidadException;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.service.EstadoFinancieroService;

/**
 * REST Resource for financial statements (Estados Financieros).
 */
@Path("estados-financieros")
public class EstadoFinancieroResource implements Serializable {
    
    @Inject
    EstadoFinancieroService estadoFinancieroService;
    
    /**
     * Get financial statements by period.
     */
    @GET
    @Path("periodo/{idPeriodo}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findByPeriodo(@PathParam("idPeriodo") UUID idPeriodo) {
        try {
            List<EstadoFinanciero> estados = estadoFinancieroService.findByPeriodo(idPeriodo);
            return Response.ok(estados).build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Get financial statement by ID.
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findById(@PathParam("id") UUID id) {
        try {
            EstadoFinanciero estado = estadoFinancieroService.findById(id);
            if (estado == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(estado).build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Generate Balance Sheet (Balance General).
     */
    @POST
    @Path("balance-general/{idPeriodo}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response generarBalanceGeneral(@PathParam("idPeriodo") UUID idPeriodo, @Context UriInfo uriInfo) {
        try {
            UUID id = estadoFinancieroService.generarBalanceGeneral(idPeriodo);
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(id.toString());
            return Response.created(uriBuilder.build()).build();
        } catch (ContabilidadException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(e.getMessage()))
                .build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Generate Income Statement (Estado de Resultados).
     */
    @POST
    @Path("estado-resultados/{idPeriodo}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response generarEstadoResultados(@PathParam("idPeriodo") UUID idPeriodo, @Context UriInfo uriInfo) {
        try {
            UUID id = estadoFinancieroService.generarEstadoResultados(idPeriodo);
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(id.toString());
            return Response.created(uriBuilder.build()).build();
        } catch (ContabilidadException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(e.getMessage()))
                .build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Finalize a financial statement.
     */
    @PUT
    @Path("{id}/finalizar")
    @Produces({MediaType.APPLICATION_JSON})
    public Response finalizarEstado(@PathParam("id") UUID id) {
        try {
            estadoFinancieroService.finalizarEstado(id);
            return Response.ok().build();
        } catch (ContabilidadException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(e.getMessage()))
                .build();
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
