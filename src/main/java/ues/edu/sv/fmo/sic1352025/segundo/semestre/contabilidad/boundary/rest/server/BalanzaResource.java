package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.BalanzaBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.BalanzaDto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Path("balanza")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Balanza de Comprobación", description = "Operaciones para la balanza de comprobación")
public class BalanzaResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    BalanzaBean balanzaBean;

    @GET
    @Path("comprobacion")
    @Operation(summary = "Generar balanza de comprobación", description = "Genera la balanza de comprobación para un rango de fechas")
    @APIResponse(responseCode = "200", description = "Balanza generada exitosamente")
    public Response generarBalanza(
            @Parameter(description = "Fecha desde (YYYY-MM-DD)") @QueryParam("fechaDesde") String fechaDesde,
            @Parameter(description = "Fecha hasta (YYYY-MM-DD)") @QueryParam("fechaHasta") String fechaHasta) {
        
        LocalDate desde = fechaDesde != null ? LocalDate.parse(fechaDesde) : LocalDate.now().withDayOfYear(1);
        LocalDate hasta = fechaHasta != null ? LocalDate.parse(fechaHasta) : LocalDate.now();
        
        List<BalanzaDto> balanza = balanzaBean.generarBalanzaComprobacion(desde, hasta);
        return Response.ok(balanza).build();
    }

    @GET
    @Path("comprobacion/export")
    @Operation(summary = "Exportar balanza", description = "Exporta la balanza de comprobación a CSV")
    @APIResponse(responseCode = "200", description = "Archivo CSV generado")
    @Produces("text/csv")
    public Response exportarBalanza(
            @Parameter(description = "Fecha desde (YYYY-MM-DD)") @QueryParam("fechaDesde") String fechaDesde,
            @Parameter(description = "Fecha hasta (YYYY-MM-DD)") @QueryParam("fechaHasta") String fechaHasta,
            @Parameter(description = "Formato de exportación") @QueryParam("format") @DefaultValue("csv") String format) {
        
        LocalDate desde = fechaDesde != null ? LocalDate.parse(fechaDesde) : LocalDate.now().withDayOfYear(1);
        LocalDate hasta = fechaHasta != null ? LocalDate.parse(fechaHasta) : LocalDate.now();
        
        List<BalanzaDto> balanza = balanzaBean.generarBalanzaComprobacion(desde, hasta);
        
        if ("csv".equalsIgnoreCase(format)) {
            String csv = balanzaBean.exportToCsv(balanza);
            return Response.ok(csv)
                .header("Content-Disposition", "attachment; filename=\"balanza_comprobacion.csv\"")
                .build();
        }
        
        // Default to JSON if format is not CSV
        return Response.ok(balanza).build();
    }
}
