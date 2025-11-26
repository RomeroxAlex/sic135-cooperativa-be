package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import java.io.Serializable;
import java.math.BigDecimal;
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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.PartidaAjusteDTO;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PartidaAjuste;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ContabilidadException;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.service.PartidaAjusteService;

/**
 * REST Resource for adjustment entries (Partidas de Ajuste).
 * MAIN FOCUS MODULE - Handles both manual and automatic adjustments.
 */
@Path("partidas-ajuste")
public class PartidaAjusteResource implements Serializable {
    
    @Inject
    PartidaAjusteService partidaAjusteService;
    
    /**
     * Get all adjustment entries.
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response findAll() {
        try {
            List<PartidaAjuste> ajustes = partidaAjusteService.findAll();
            return Response.ok(ajustes).build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Get adjustment entry by ID.
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findById(@PathParam("id") UUID id) {
        try {
            PartidaAjuste ajuste = partidaAjusteService.findById(id);
            if (ajuste == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(ajuste).build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Get adjustment entries by period.
     */
    @GET
    @Path("periodo/{idPeriodo}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findByPeriodo(@PathParam("idPeriodo") UUID idPeriodo) {
        try {
            List<PartidaAjuste> ajustes = partidaAjusteService.findByPeriodo(idPeriodo);
            return Response.ok(ajustes).build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Get pending adjustment entries.
     */
    @GET
    @Path("pendientes")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findPendientes() {
        try {
            List<PartidaAjuste> ajustes = partidaAjusteService.findPendientes();
            return Response.ok(ajustes).build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Get adjustment entries by type.
     */
    @GET
    @Path("tipo/{tipo}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findByTipo(@PathParam("tipo") String tipo) {
        try {
            List<PartidaAjuste> ajustes = partidaAjusteService.findByTipo(tipo);
            return Response.ok(ajustes).build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Create a manual adjustment entry.
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response crearAjuste(PartidaAjusteDTO dto, @Context UriInfo uriInfo) {
        try {
            UUID id = partidaAjusteService.crearPartidaAjuste(dto);
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
     * Apply an adjustment entry (generates journal entry).
     */
    @PUT
    @Path("{id}/aplicar")
    @Produces({MediaType.APPLICATION_JSON})
    public Response aplicarAjuste(@PathParam("id") UUID id) {
        try {
            UUID idPartida = partidaAjusteService.aplicarAjuste(id);
            return Response.ok(new AplicarResponse(idPartida)).build();
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
     * Void an adjustment entry.
     */
    @PUT
    @Path("{id}/anular")
    @Produces({MediaType.APPLICATION_JSON})
    public Response anularAjuste(@PathParam("id") UUID id) {
        try {
            partidaAjusteService.anularAjuste(id);
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
     * Generate automatic depreciation adjustment.
     */
    @POST
    @Path("depreciacion")
    @Produces({MediaType.APPLICATION_JSON})
    public Response generarDepreciacion(
            @QueryParam("idPeriodo") UUID idPeriodo,
            @QueryParam("idCuentaDepreciacion") UUID idCuentaDepreciacion,
            @QueryParam("idCuentaAcumulada") UUID idCuentaAcumulada,
            @QueryParam("monto") BigDecimal monto,
            @QueryParam("descripcion") String descripcion,
            @Context UriInfo uriInfo) {
        try {
            UUID id = partidaAjusteService.generarDepreciacionAutomatica(
                idPeriodo, idCuentaDepreciacion, idCuentaAcumulada, monto, descripcion);
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
     * Generate automatic provision adjustment.
     */
    @POST
    @Path("provision")
    @Produces({MediaType.APPLICATION_JSON})
    public Response generarProvision(
            @QueryParam("idPeriodo") UUID idPeriodo,
            @QueryParam("idCuentaGasto") UUID idCuentaGasto,
            @QueryParam("idCuentaProvision") UUID idCuentaProvision,
            @QueryParam("monto") BigDecimal monto,
            @QueryParam("descripcion") String descripcion,
            @Context UriInfo uriInfo) {
        try {
            UUID id = partidaAjusteService.generarProvisionAutomatica(
                idPeriodo, idCuentaGasto, idCuentaProvision, monto, descripcion);
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
    
    /**
     * Apply response class
     */
    public static class AplicarResponse {
        private UUID idPartidaDiario;
        
        public AplicarResponse(UUID idPartidaDiario) {
            this.idPartidaDiario = idPartidaDiario;
        }
        
        public UUID getIdPartidaDiario() {
            return idPartidaDiario;
        }
        
        public void setIdPartidaDiario(UUID idPartidaDiario) {
            this.idPartidaDiario = idPartidaDiario;
        }
    }
}
