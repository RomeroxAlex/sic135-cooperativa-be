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
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.LibroMayor;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ContabilidadException;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.service.LibroMayorService;

/**
 * REST Resource for general ledger (Libro Mayor / Mayorización).
 */
@Path("libro-mayor")
public class LibroMayorResource implements Serializable {
    
    @Inject
    LibroMayorService libroMayorService;
    
    /**
     * Get general ledger entries by period.
     */
    @GET
    @Path("periodo/{idPeriodo}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findByPeriodo(@PathParam("idPeriodo") UUID idPeriodo) {
        try {
            List<LibroMayor> libroMayor = libroMayorService.findByPeriodo(idPeriodo);
            return Response.ok(libroMayor).build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Get general ledger entries with non-zero balances for a period.
     */
    @GET
    @Path("periodo/{idPeriodo}/con-saldo")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findByPeriodoConSaldo(@PathParam("idPeriodo") UUID idPeriodo) {
        try {
            List<LibroMayor> libroMayor = libroMayorService.findByPeriodoConSaldo(idPeriodo);
            return Response.ok(libroMayor).build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Get ledger history for a specific account.
     */
    @GET
    @Path("cuenta/{idCuenta}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findByCuenta(@PathParam("idCuenta") UUID idCuenta) {
        try {
            List<LibroMayor> libroMayor = libroMayorService.findByCuenta(idCuenta);
            return Response.ok(libroMayor).build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Get current balance for an account in a period.
     */
    @GET
    @Path("saldo/cuenta/{idCuenta}/periodo/{idPeriodo}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getSaldoCuenta(@PathParam("idCuenta") UUID idCuenta, @PathParam("idPeriodo") UUID idPeriodo) {
        try {
            BigDecimal saldo = libroMayorService.getSaldoCuenta(idCuenta, idPeriodo);
            return Response.ok(new SaldoResponse(saldo)).build();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return Response.serverError().build();
        }
    }
    
    /**
     * Generate/update ledgerization for a period.
     */
    @POST
    @Path("generar/{idPeriodo}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response generarMayorizacion(@PathParam("idPeriodo") UUID idPeriodo) {
        try {
            libroMayorService.generarMayorizacion(idPeriodo);
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
     * Balance response class
     */
    public static class SaldoResponse {
        private BigDecimal saldo;
        
        public SaldoResponse(BigDecimal saldo) {
            this.saldo = saldo;
        }
        
        public BigDecimal getSaldo() {
            return saldo;
        }
        
        public void setSaldo(BigDecimal saldo) {
            this.saldo = saldo;
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
