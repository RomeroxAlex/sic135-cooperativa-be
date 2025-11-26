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
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CabeceraTransaccionBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CabeceraTransaccion;

@Path("cabeceras-transacciones")
public class CabeceraTransaccionResource {

    @Inject
    CabeceraTransaccionBean cabeceraTransaccionBean;

    /**
     * GET /cabeceras-transacciones - Obtener todas las cabeceras de transacciones
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response findAll(){
        try {
            List<CabeceraTransaccion> listCabeceraTransaccion = cabeceraTransaccionBean.findAll();
            return Response.ok(listCabeceraTransaccion).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    /**
     * GET /cabeceras-transacciones/{id} - Obtener cabecera de transacción por ID
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findById(@PathParam("id") String id) {
        try {
            UUID idCabecera = UUID.fromString(id);
            CabeceraTransaccion cabecera = cabeceraTransaccionBean.findById(idCabecera);
            if (cabecera == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Cabecera de transacción no encontrada")
                        .build();
            }
            return Response.ok(cabecera).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID inválido")
                    .build();
        } catch (Exception ex) {
            return Response.serverError().entity(ex.getMessage()).build();
        }
    }

    /**
     * POST /cabeceras-transacciones - Crear cabecera de asiento contable
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response create(CabeceraTransaccion cabeceraTransaccion, @Context UriInfo uriInfo) {
        try {
            if (cabeceraTransaccion == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Datos de cabecera de transacción inválidos")
                        .build();
            }

            UUID idCabecera = UUID.randomUUID();
            cabeceraTransaccion.setIdCabeceraTransaccion(idCabecera);
            cabeceraTransaccionBean.persistEntity(cabeceraTransaccion);

            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(idCabecera.toString());
            return Response.created(uriBuilder.build()).entity(cabeceraTransaccion).build();
        } catch (Exception ex) {
            return Response.serverError()
                    .entity("Error al crear cabecera de transacción: " + ex.getMessage())
                    .build();
        }
    }

}
