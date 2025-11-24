package com.tuempresa.contabilidad.resource;

import com.tuempresa.contabilidad.dto.common.PagedResult;
import com.tuempresa.contabilidad.dto.cuenta.CuentaDto;
import com.tuempresa.contabilidad.entity.TipoCuenta;
import com.tuempresa.contabilidad.service.CuentaService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/catalogo-cuentas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Catálogo de Cuentas", description = "Operaciones relacionadas con el catálogo de cuentas contables.")
public class CuentaResource {

    @Inject
    private CuentaService cuentaService;

    @GET
    @Operation(summary = "Listar todas las cuentas", description = "Obtiene una lista de todas las cuentas del catálogo.")
    @APIResponse(responseCode = "200", description = "Lista de cuentas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedResult.class)))
    public Response getAllCuentas(
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("tipo") TipoCuenta tipo
    ) {
        return Response.ok(cuentaService.getAllCuentas(page, size, tipo)).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Obtener cuenta por ID", description = "Busca y devuelve una cuenta específica por su ID.")
    @APIResponse(responseCode = "200", description = "Cuenta encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CuentaDto.class)))
    @APIResponse(responseCode = "404", description = "Cuenta no encontrada")
    public Response getCuentaById(@PathParam("id") Long id) {
        return cuentaService.getCuentaById(id)
                .map(cuenta -> Response.ok(cuenta).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Operation(summary = "Crear una nueva cuenta", description = "Crea una nueva cuenta en el catálogo.")
    @APIResponse(responseCode = "201", description = "Cuenta creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CuentaDto.class)))
    @APIResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @APIResponse(responseCode = "409", description = "La cuenta ya existe (conflicto de código)")
    public Response createCuenta(@Valid CuentaDto cuentaDto) {
        try {
            CuentaDto createdCuenta = cuentaService.createCuenta(cuentaDto);
            return Response.status(Response.Status.CREATED).entity(createdCuenta).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Actualizar una cuenta existente", description = "Actualiza los datos de una cuenta existente en el catálogo.")
    @APIResponse(responseCode = "200", description = "Cuenta actualizada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CuentaDto.class)))
    @APIResponse(responseCode = "400", description = "Datos de entrada inválidos")
    @APIResponse(responseCode = "404", description = "Cuenta no encontrada")
    @APIResponse(responseCode = "409", description = "Conflicto con otra cuenta (ej. código duplicado)")
    public Response updateCuenta(@PathParam("id") Long id, @Valid CuentaDto cuentaDto) {
        try {
            CuentaDto updatedCuenta = cuentaService.updateCuenta(id, cuentaDto);
            return Response.ok(updatedCuenta).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Eliminar una cuenta", description = "Elimina una cuenta del catálogo por su ID.")
    @APIResponse(responseCode = "204", description = "Cuenta eliminada exitosamente")
    @APIResponse(responseCode = "404", description = "Cuenta no encontrada")
    public Response deleteCuenta(@PathParam("id") Long id) {
        try {
            cuentaService.deleteCuenta(id);
            return Response.noContent().build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}
