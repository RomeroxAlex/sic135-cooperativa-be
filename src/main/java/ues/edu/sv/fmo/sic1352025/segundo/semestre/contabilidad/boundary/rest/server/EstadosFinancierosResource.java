package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.EstadosFinancierosBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.BalanceGeneralDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.EstadoResultadoDto;

import java.io.Serializable;
import java.time.LocalDate;

@Path("estados")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Estados Financieros", description = "Generación de estados financieros")
public class EstadosFinancierosResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    EstadosFinancierosBean estadosFinancierosBean;

    @GET
    @Path("balance-general")
    @Operation(summary = "Generar Balance General", description = "Genera el balance general para un periodo")
    @APIResponse(responseCode = "200", description = "Balance general generado exitosamente")
    public Response generarBalanceGeneral(
            @Parameter(description = "Periodo (YYYY o YYYY-MM)") @QueryParam("periodo") String periodo) {
        
        if (periodo == null || periodo.isEmpty()) {
            periodo = String.valueOf(LocalDate.now().getYear());
        }
        
        BalanceGeneralDto balance = estadosFinancierosBean.generarBalanceGeneral(periodo);
        return Response.ok(balance).build();
    }

    @GET
    @Path("resultado")
    @Operation(summary = "Generar Estado de Resultados", description = "Genera el estado de pérdidas y ganancias para un periodo")
    @APIResponse(responseCode = "200", description = "Estado de resultados generado exitosamente")
    public Response generarEstadoResultado(
            @Parameter(description = "Periodo (YYYY o YYYY-MM)") @QueryParam("periodo") String periodo) {
        
        if (periodo == null || periodo.isEmpty()) {
            periodo = String.valueOf(LocalDate.now().getYear());
        }
        
        EstadoResultadoDto estado = estadosFinancierosBean.generarEstadoResultado(periodo);
        return Response.ok(estado).build();
    }
}
