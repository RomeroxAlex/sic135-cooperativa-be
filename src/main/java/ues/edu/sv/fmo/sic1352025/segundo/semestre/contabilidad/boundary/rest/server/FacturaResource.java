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
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.FacturaDTO;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.Factura;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.ReporteVentas;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ContabilidadException;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.service.FacturaService;

/**
 * REST Resource for invoices (Facturas) and daily sales reports.
 */
@Path("facturas")
public class FacturaResource implements Serializable {
    
    @Inject
    FacturaService facturaService;
    
    /**
     * Get all invoices.
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response findAll() {
        try {
            List<Factura> facturas = facturaService.findAll();
            return Response.ok(facturas).build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Get invoice by ID.
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findById(@PathParam("id") UUID id) {
        try {
            Factura factura = facturaService.findById(id);
            if (factura == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(factura).build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Create a new invoice.
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response crearFactura(FacturaDTO dto, @Context UriInfo uriInfo) {
        try {
            UUID id = facturaService.crearFactura(dto);
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
     * Emit an invoice.
     */
    @PUT
    @Path("{id}/emitir")
    @Produces({MediaType.APPLICATION_JSON})
    public Response emitirFactura(@PathParam("id") UUID id) {
        try {
            facturaService.emitirFactura(id);
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
     * Void an invoice.
     */
    @PUT
    @Path("{id}/anular")
    @Produces({MediaType.APPLICATION_JSON})
    public Response anularFactura(@PathParam("id") UUID id) {
        try {
            facturaService.anularFactura(id);
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
     * Generate daily sales report.
     */
    @POST
    @Path("reporte-diario")
    @Produces({MediaType.APPLICATION_JSON})
    public Response generarReporteDiario(@QueryParam("fecha") Date fecha, @Context UriInfo uriInfo) {
        try {
            Date reportDate = fecha != null ? fecha : new Date();
            UUID id = facturaService.generarReporteDiario(reportDate);
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
     * Get daily sales report by date.
     */
    @GET
    @Path("reporte-diario")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getReporteDiario(@QueryParam("fecha") Date fecha) {
        try {
            Date reportDate = fecha != null ? fecha : new Date();
            ReporteVentas reporte = facturaService.getReporteDiario(reportDate);
            if (reporte == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            return Response.ok(reporte).build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Get all daily sales reports.
     */
    @GET
    @Path("reportes-diarios")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllReportes() {
        try {
            List<ReporteVentas> reportes = facturaService.findAllReportes();
            return Response.ok(reportes).build();
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
