package com.tuempresa.contabilidad.resource;

import com.tuempresa.contabilidad.dto.ManualDto;
import com.tuempresa.contabilidad.service.ManualCuentaService;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/manual-cuentas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Manual de Cuentas", description = "Operaciones relacionadas con el manual de cuentas.")
public class ManualCuentaResource {

    @Inject
    private ManualCuentaService manualService;

    @GET
    public Response getManuales() {
        return Response.ok(manualService.getAllManuales()).build();
    }

    @POST
    public Response createManual(@Valid ManualDto manualDto) {
        ManualDto createdManual = manualService.createManual(manualDto);
        return Response.status(Response.Status.CREATED).entity(createdManual).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateManual(@PathParam("id") Long id, @Valid ManualDto manualDto) {
        try {
            ManualDto updatedManual = manualService.updateManual(id, manualDto);
            return Response.ok(updatedManual).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}
