package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import java.io.Serializable;
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
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.PartidaDiarioDTO;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.DetallePartidaDiario;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PartidaDiario;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ContabilidadException;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.service.PartidaDiarioService;

/**
 * REST Resource for journal entries (Libro Diario).
 */
@Path("partidas-diario")
public class PartidaDiarioResource implements Serializable {
    
    @Inject
    PartidaDiarioService partidaDiarioService;
    
    /**
     * Get all journal entries.
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response findAll() {
        try {
            List<PartidaDiario> partidas = partidaDiarioService.findAll();
            return Response.ok(partidas).build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Get journal entry by ID.
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findById(@PathParam("id") UUID id) {
        try {
            PartidaDiario partida = partidaDiarioService.findById(id);
            if (partida == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(partida).build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Get journal entries by period.
     */
    @GET
    @Path("periodo/{idPeriodo}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findByPeriodo(@PathParam("idPeriodo") UUID idPeriodo) {
        try {
            List<PartidaDiario> partidas = partidaDiarioService.findByPeriodo(idPeriodo);
            return Response.ok(partidas).build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Get detail lines for a journal entry.
     */
    @GET
    @Path("{id}/detalles")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findDetalles(@PathParam("id") UUID id) {
        try {
            List<DetallePartidaDiario> detalles = partidaDiarioService.findDetallesByPartida(id);
            return Response.ok(detalles).build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Create a new journal entry.
     * Validates that debe = haber before saving.
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response crearPartida(PartidaDiarioDTO dto, @Context UriInfo uriInfo) {
        try {
            UUID id = partidaDiarioService.crearPartidaDiario(dto);
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
     * Post a journal entry (change status to CONTABILIZADA).
     */
    @PUT
    @Path("{id}/contabilizar")
    @Produces({MediaType.APPLICATION_JSON})
    public Response contabilizar(@PathParam("id") UUID id) {
        try {
            partidaDiarioService.contabilizarPartida(id);
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
     * Void a journal entry.
     */
    @PUT
    @Path("{id}/anular")
    @Produces({MediaType.APPLICATION_JSON})
    public Response anular(@PathParam("id") UUID id) {
        try {
            partidaDiarioService.anularPartida(id);
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
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
}
