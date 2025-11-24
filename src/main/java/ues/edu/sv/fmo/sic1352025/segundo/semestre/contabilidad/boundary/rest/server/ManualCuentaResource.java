package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.ManualCuentaBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.ManualDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.ManualCuenta;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ResourceNotFoundException;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("manual-cuentas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Manual de Cuentas", description = "Operaciones para el manual de cuentas contables")
public class ManualCuentaResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    ManualCuentaBean manualCuentaBean;

    @GET
    @Operation(summary = "Listar manuales", description = "Obtiene todos los manuales de cuentas")
    @APIResponse(responseCode = "200", description = "Lista de manuales obtenida exitosamente")
    public Response findAll() {
        List<ManualCuenta> manuales = manualCuentaBean.findAllActive();
        List<ManualDto> dtos = manuales.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
        return Response.ok(dtos).build();
    }

    @GET
    @Path("{id}")
    @Operation(summary = "Obtener manual por ID", description = "Obtiene un manual específico")
    @APIResponse(responseCode = "200", description = "Manual encontrado")
    @APIResponse(responseCode = "404", description = "Manual no encontrado")
    public Response findById(@PathParam("id") UUID id) {
        ManualCuenta manual = manualCuentaBean.findById(id);
        if (manual == null) {
            throw new ResourceNotFoundException("ManualCuenta", id);
        }
        return Response.ok(toDto(manual)).build();
    }

    @POST
    @Operation(summary = "Crear manual", description = "Crea un nuevo manual de cuentas")
    @APIResponse(responseCode = "201", description = "Manual creado exitosamente")
    public Response create(@Valid ManualDto dto, @Context UriInfo uriInfo) {
        ManualCuenta manual = toEntity(dto);
        manual = manualCuentaBean.create(manual);
        
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(manual.getId().toString());
        return Response.created(uriBuilder.build()).entity(toDto(manual)).build();
    }

    @PUT
    @Path("{id}")
    @Operation(summary = "Actualizar manual", description = "Actualiza un manual existente")
    @APIResponse(responseCode = "200", description = "Manual actualizado exitosamente")
    @APIResponse(responseCode = "404", description = "Manual no encontrado")
    public Response update(@PathParam("id") UUID id, @Valid ManualDto dto) {
        ManualCuenta manual = toEntity(dto);
        manual = manualCuentaBean.update(id, manual);
        return Response.ok(toDto(manual)).build();
    }

    private ManualDto toDto(ManualCuenta entity) {
        ManualDto dto = new ManualDto();
        dto.setId(entity.getId());
        dto.setTitulo(entity.getTitulo());
        dto.setContenido(entity.getContenido());
        dto.setVersion(entity.getVersion());
        dto.setFechaCreacion(entity.getFechaCreacion());
        return dto;
    }

    private ManualCuenta toEntity(ManualDto dto) {
        ManualCuenta entity = new ManualCuenta();
        entity.setId(dto.getId());
        entity.setTitulo(dto.getTitulo());
        entity.setContenido(dto.getContenido());
        entity.setVersion(dto.getVersion());
        return entity;
    }
}
