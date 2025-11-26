package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.BalanzaComprobacion;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ContabilidadException;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.service.BalanzaComprobacionService;

/**
 * REST Resource for trial balance (Balanza de Comprobación).
 */
@Path("balanza-comprobacion")
public class BalanzaComprobacionResource implements Serializable {
    
    @Inject
    BalanzaComprobacionService balanzaComprobacionService;
    
    /**
     * Get trial balance entries by period.
     */
    @GET
    @Path("periodo/{idPeriodo}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findByPeriodo(@PathParam("idPeriodo") UUID idPeriodo) {
        try {
            List<BalanzaComprobacion> balanza = balanzaComprobacionService.findByPeriodo(idPeriodo);
            return Response.ok(balanza).build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Get trial balance totals for a period.
     */
    @GET
    @Path("periodo/{idPeriodo}/totales")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getTotales(@PathParam("idPeriodo") UUID idPeriodo) {
        try {
            BigDecimal[] totales = balanzaComprobacionService.getTotalesBalanza(idPeriodo);
            TotalesResponse response = new TotalesResponse(
                totales[0], totales[1], totales[2], totales[3], totales[4], totales[5]
            );
            return Response.ok(response).build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Verify if trial balance is balanced.
     */
    @GET
    @Path("periodo/{idPeriodo}/verificar")
    @Produces({MediaType.APPLICATION_JSON})
    public Response verificarBalance(@PathParam("idPeriodo") UUID idPeriodo) {
        try {
            boolean balanced = balanzaComprobacionService.verificarBalance(idPeriodo);
            return Response.ok(new VerificacionResponse(balanced)).build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Generate trial balance for a period.
     */
    @POST
    @Path("generar/{idPeriodo}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response generarBalanza(@PathParam("idPeriodo") UUID idPeriodo) {
        try {
            boolean balanced = balanzaComprobacionService.generarBalanzaComprobacion(idPeriodo);
            return Response.ok(new GeneracionResponse(balanced)).build();
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
     * Totals response class
     */
    public static class TotalesResponse {
        private BigDecimal saldoInicialDebe;
        private BigDecimal saldoInicialHaber;
        private BigDecimal movimientosDebe;
        private BigDecimal movimientosHaber;
        private BigDecimal saldoFinalDebe;
        private BigDecimal saldoFinalHaber;
        
        public TotalesResponse(BigDecimal saldoInicialDebe, BigDecimal saldoInicialHaber,
                              BigDecimal movimientosDebe, BigDecimal movimientosHaber,
                              BigDecimal saldoFinalDebe, BigDecimal saldoFinalHaber) {
            this.saldoInicialDebe = saldoInicialDebe;
            this.saldoInicialHaber = saldoInicialHaber;
            this.movimientosDebe = movimientosDebe;
            this.movimientosHaber = movimientosHaber;
            this.saldoFinalDebe = saldoFinalDebe;
            this.saldoFinalHaber = saldoFinalHaber;
        }
        
        public BigDecimal getSaldoInicialDebe() { return saldoInicialDebe; }
        public void setSaldoInicialDebe(BigDecimal v) { this.saldoInicialDebe = v; }
        public BigDecimal getSaldoInicialHaber() { return saldoInicialHaber; }
        public void setSaldoInicialHaber(BigDecimal v) { this.saldoInicialHaber = v; }
        public BigDecimal getMovimientosDebe() { return movimientosDebe; }
        public void setMovimientosDebe(BigDecimal v) { this.movimientosDebe = v; }
        public BigDecimal getMovimientosHaber() { return movimientosHaber; }
        public void setMovimientosHaber(BigDecimal v) { this.movimientosHaber = v; }
        public BigDecimal getSaldoFinalDebe() { return saldoFinalDebe; }
        public void setSaldoFinalDebe(BigDecimal v) { this.saldoFinalDebe = v; }
        public BigDecimal getSaldoFinalHaber() { return saldoFinalHaber; }
        public void setSaldoFinalHaber(BigDecimal v) { this.saldoFinalHaber = v; }
    }
    
    /**
     * Verification response class
     */
    public static class VerificacionResponse {
        private boolean balanced;
        
        public VerificacionResponse(boolean balanced) {
            this.balanced = balanced;
        }
        
        public boolean isBalanced() { return balanced; }
        public void setBalanced(boolean balanced) { this.balanced = balanced; }
    }
    
    /**
     * Generation response class
     */
    public static class GeneracionResponse {
        private boolean balanced;
        
        public GeneracionResponse(boolean balanced) {
            this.balanced = balanced;
        }
        
        public boolean isBalanced() { return balanced; }
        public void setBalanced(boolean balanced) { this.balanced = balanced; }
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
