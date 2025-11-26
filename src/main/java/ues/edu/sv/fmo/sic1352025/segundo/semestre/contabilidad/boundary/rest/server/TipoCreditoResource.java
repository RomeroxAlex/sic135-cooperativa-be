package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import java.util.List;
import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.TipoCreditoBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.TipoCredito;

@Path("tipos-credito")
public class TipoCreditoResource {

    @Inject
    TipoCreditoBean tipoCreditoBean;

    /**
     * GET /tipos-credito - Obtener todos los tipos de crédito
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response findAll() {
        try {
            List<TipoCredito> listTiposCredito = tipoCreditoBean.findAll();
            if (listTiposCredito == null) {
                return Response.serverError().build();
            }
            return Response.ok(listTiposCredito).build();
        } catch (Exception ex) {
            return Response.serverError().entity(ex.getMessage()).build();
        }
    }

    /**
     * GET /tipos-credito/{id} - Obtener tipo de crédito por ID
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findById(@PathParam("id") String id) {
        try {
            UUID idTipoCredito = UUID.fromString(id);
            TipoCredito tipoCredito = tipoCreditoBean.findById(idTipoCredito);
            if (tipoCredito == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Tipo de crédito no encontrado")
                        .build();
            }
            return Response.ok(tipoCredito).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID inválido")
                    .build();
        } catch (Exception ex) {
            return Response.serverError().entity(ex.getMessage()).build();
        }
    }

    /**
     * POST /tipos-credito - Crear tipo de crédito
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response create(TipoCredito tipoCredito, @Context UriInfo uriInfo) {
        try {
            if (tipoCredito == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Datos de tipo de crédito inválidos")
                        .build();
            }

            UUID idTipoCredito = UUID.randomUUID();
            tipoCredito.setIdTipoCredito(idTipoCredito);
            tipoCreditoBean.persistEntity(tipoCredito);

            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(idTipoCredito.toString());
            return Response.created(uriBuilder.build()).entity(tipoCredito).build();
        } catch (Exception ex) {
            return Response.serverError()
                    .entity("Error al crear tipo de crédito: " + ex.getMessage())
                    .build();
        }
    }
}
