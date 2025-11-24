package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CatalogoCuentaBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.CuentaDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.PagedResponseDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CatalogoCuenta;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ResourceNotFoundException;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("catalogo-cuentas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Catálogo de Cuentas", description = "Operaciones CRUD para el catálogo de cuentas contables")
public class CatalogoCuentaResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    CatalogoCuentaBean catalogoCuentaBean;

    @GET
    @Operation(summary = "Listar cuentas", description = "Obtiene lista paginada de cuentas contables")
    @APIResponse(responseCode = "200", description = "Lista de cuentas obtenida exitosamente")
    public Response findAll(
            @Parameter(description = "Número de página") @QueryParam("page") @DefaultValue("0") int page,
            @Parameter(description = "Tamaño de página") @QueryParam("size") @DefaultValue("20") int size,
            @Parameter(description = "Filtro por tipo de cuenta") @QueryParam("tipo") String tipo) {
        
        List<CatalogoCuenta> cuentas = catalogoCuentaBean.findPaginated(page, size, tipo);
        long total = catalogoCuentaBean.count();
        
        List<CuentaDto> dtos = cuentas.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
        
        PagedResponseDto<CuentaDto> response = new PagedResponseDto<>(dtos, page, size, total);
        return Response.ok(response).build();
    }

    @GET
    @Path("{id}")
    @Operation(summary = "Obtener cuenta por ID", description = "Obtiene una cuenta contable específica")
    @APIResponse(responseCode = "200", description = "Cuenta encontrada")
    @APIResponse(responseCode = "404", description = "Cuenta no encontrada")
    public Response findById(@Parameter(description = "ID de la cuenta") @PathParam("id") UUID id) {
        CatalogoCuenta cuenta = catalogoCuentaBean.findById(id);
        if (cuenta == null) {
            throw new ResourceNotFoundException("CatalogoCuenta", id);
        }
        return Response.ok(toDto(cuenta)).build();
    }

    @POST
    @Operation(summary = "Crear cuenta", description = "Crea una nueva cuenta contable")
    @APIResponse(responseCode = "201", description = "Cuenta creada exitosamente")
    @APIResponse(responseCode = "400", description = "Datos inválidos")
    public Response create(@Valid CuentaDto dto, @Context UriInfo uriInfo) {
        CatalogoCuenta cuenta = toEntity(dto);
        cuenta = catalogoCuentaBean.create(cuenta);
        
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(cuenta.getId().toString());
        return Response.created(uriBuilder.build()).entity(toDto(cuenta)).build();
    }

    @PUT
    @Path("{id}")
    @Operation(summary = "Actualizar cuenta", description = "Actualiza una cuenta contable existente")
    @APIResponse(responseCode = "200", description = "Cuenta actualizada exitosamente")
    @APIResponse(responseCode = "404", description = "Cuenta no encontrada")
    public Response update(
            @Parameter(description = "ID de la cuenta") @PathParam("id") UUID id, 
            @Valid CuentaDto dto) {
        CatalogoCuenta cuenta = toEntity(dto);
        cuenta = catalogoCuentaBean.update(id, cuenta);
        return Response.ok(toDto(cuenta)).build();
    }

    @DELETE
    @Path("{id}")
    @Operation(summary = "Eliminar cuenta", description = "Elimina (soft-delete) una cuenta contable")
    @APIResponse(responseCode = "204", description = "Cuenta eliminada exitosamente")
    @APIResponse(responseCode = "404", description = "Cuenta no encontrada")
    public Response delete(@Parameter(description = "ID de la cuenta") @PathParam("id") UUID id) {
        catalogoCuentaBean.softDelete(id);
        return Response.noContent().build();
    }

    private CuentaDto toDto(CatalogoCuenta entity) {
        CuentaDto dto = new CuentaDto();
        dto.setId(entity.getId());
        dto.setCodigo(entity.getCodigo());
        dto.setNombre(entity.getNombre());
        dto.setTipo(entity.getTipo());
        dto.setNivel(entity.getNivel());
        dto.setDescripcion(entity.getDescripcion());
        if (entity.getCuentaPadre() != null) {
            dto.setCuentaPadreId(entity.getCuentaPadre().getId());
        }
        dto.setFechaCreacion(entity.getFechaCreacion());
        return dto;
    }

    private CatalogoCuenta toEntity(CuentaDto dto) {
        CatalogoCuenta entity = new CatalogoCuenta();
        entity.setId(dto.getId());
        entity.setCodigo(dto.getCodigo());
        entity.setNombre(dto.getNombre());
        entity.setTipo(dto.getTipo());
        entity.setNivel(dto.getNivel());
        entity.setDescripcion(dto.getDescripcion());
        if (dto.getCuentaPadreId() != null) {
            CatalogoCuenta padre = catalogoCuentaBean.findById(dto.getCuentaPadreId());
            entity.setCuentaPadre(padre);
        }
        return entity;
    }
}
