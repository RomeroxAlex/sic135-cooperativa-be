package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.AuditoriaBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.AuditoriaDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.PagedResponseDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.Auditoria;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Path("auditoria")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Auditoría", description = "Registro de auditoría y logs del sistema")
public class AuditoriaResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    AuditoriaBean auditoriaBean;

    @GET
    @Operation(summary = "Listar registros de auditoría", description = "Obtiene lista paginada de registros de auditoría")
    @APIResponse(responseCode = "200", description = "Lista de auditoría obtenida exitosamente")
    public Response findAll(
            @Parameter(description = "Número de página") @QueryParam("page") @DefaultValue("0") int page,
            @Parameter(description = "Tamaño de página") @QueryParam("size") @DefaultValue("50") int size,
            @Parameter(description = "Filtro por usuario") @QueryParam("usuario") String usuario,
            @Parameter(description = "Filtro por entidad") @QueryParam("entidad") String entidad) {
        
        List<Auditoria> auditorias = auditoriaBean.findPaginated(page, size, usuario, entidad);
        
        List<AuditoriaDto> dtos = auditorias.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
        
        return Response.ok(dtos).build();
    }

    @GET
    @Path("usuario/{usuario}")
    @Operation(summary = "Obtener auditoría por usuario", description = "Obtiene registros de auditoría de un usuario específico")
    @APIResponse(responseCode = "200", description = "Registros encontrados")
    public Response findByUsuario(@PathParam("usuario") String usuario) {
        List<Auditoria> auditorias = auditoriaBean.findByUsuario(usuario);
        List<AuditoriaDto> dtos = auditorias.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
        return Response.ok(dtos).build();
    }

    @GET
    @Path("entidad/{entidad}")
    @Operation(summary = "Obtener auditoría por entidad", description = "Obtiene registros de auditoría de una entidad específica")
    @APIResponse(responseCode = "200", description = "Registros encontrados")
    public Response findByEntidad(@PathParam("entidad") String entidad) {
        List<Auditoria> auditorias = auditoriaBean.findByEntidad(entidad);
        List<AuditoriaDto> dtos = auditorias.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
        return Response.ok(dtos).build();
    }

    private AuditoriaDto toDto(Auditoria entity) {
        AuditoriaDto dto = new AuditoriaDto();
        dto.setId(entity.getId());
        dto.setEntidad(entity.getEntidad());
        dto.setEntidadId(entity.getEntidadId());
        dto.setAccion(entity.getAccion());
        dto.setUsuario(entity.getUsuario());
        dto.setFechaAccion(entity.getFechaAccion());
        dto.setDatosAnteriores(entity.getDatosAnteriores());
        dto.setDatosNuevos(entity.getDatosNuevos());
        dto.setIpAddress(entity.getIpAddress());
        dto.setDetalle(entity.getDetalle());
        return dto;
    }
}
