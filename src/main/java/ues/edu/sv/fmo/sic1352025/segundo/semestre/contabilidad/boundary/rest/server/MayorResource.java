package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.MayorBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.MayorResponseDto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Path("mayor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Mayorización", description = "Operaciones para el libro mayor")
public class MayorResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    MayorBean mayorBean;

    @GET
    @Path("{cuentaId}")
    @Operation(summary = "Obtener mayor de cuenta", description = "Obtiene el libro mayor para una cuenta en un rango de fechas")
    @APIResponse(responseCode = "200", description = "Mayor obtenido exitosamente")
    @APIResponse(responseCode = "404", description = "Cuenta no encontrada")
    public Response getMayorByCuenta(
            @Parameter(description = "ID de la cuenta") @PathParam("cuentaId") UUID cuentaId,
            @Parameter(description = "Fecha inicio (YYYY-MM-DD)") @QueryParam("fechaInicio") String fechaInicio,
            @Parameter(description = "Fecha fin (YYYY-MM-DD)") @QueryParam("fechaFin") String fechaFin) {
        
        LocalDate inicio = fechaInicio != null ? LocalDate.parse(fechaInicio) : LocalDate.now().withDayOfMonth(1);
        LocalDate fin = fechaFin != null ? LocalDate.parse(fechaFin) : LocalDate.now();
        
        MayorResponseDto mayor = mayorBean.getMayorByCuenta(cuentaId, inicio, fin);
        return Response.ok(mayor).build();
    }

    @POST
    @Path("generar")
    @Operation(summary = "Generar mayorización", description = "Genera la mayorización batch para un periodo")
    @APIResponse(responseCode = "200", description = "Mayorización generada exitosamente")
    public Response generarMayorizacion(
            @Parameter(description = "Periodo (YYYY o YYYY-MM)") @QueryParam("periodo") String periodo) {
        
        if (periodo == null || periodo.isEmpty()) {
            periodo = String.valueOf(LocalDate.now().getYear());
        }
        
        mayorBean.generarMayorizacion(periodo);
        return Response.ok().entity("{\"message\": \"Mayorización generada para periodo: " + periodo + "\"}").build();
    }
}
