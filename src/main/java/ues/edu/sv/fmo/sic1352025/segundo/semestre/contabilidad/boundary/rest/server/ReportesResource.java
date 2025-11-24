package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.FacturaBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.VentaDiariaDto;

import java.io.Serializable;
import java.time.LocalDate;

@Path("reportes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Reportes", description = "Reportes de ventas y otros")
public class ReportesResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    FacturaBean facturaBean;

    @GET
    @Path("ventas-diarias")
    @Operation(summary = "Reporte de ventas diarias", description = "Obtiene el reporte de ventas para una fecha")
    @APIResponse(responseCode = "200", description = "Reporte generado exitosamente")
    public Response getVentasDiarias(
            @Parameter(description = "Fecha (YYYY-MM-DD)") @QueryParam("fecha") String fecha) {
        
        LocalDate fechaReporte = fecha != null ? LocalDate.parse(fecha) : LocalDate.now();
        VentaDiariaDto reporte = facturaBean.getReporteVentasDiarias(fechaReporte);
        return Response.ok(reporte).build();
    }
}
