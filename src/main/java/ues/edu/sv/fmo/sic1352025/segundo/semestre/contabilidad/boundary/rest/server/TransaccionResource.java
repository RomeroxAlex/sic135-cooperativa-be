package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.TransaccionBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.Transaccion;
import jakarta.inject.Inject;

@Path("transacciones")
public class TransaccionResource implements Serializable{

    @Inject
    TransaccionBean transaccionBean;

    /**
     * GET /transacciones - Obtener todas las transacciones
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response findAll(){
        List<Transaccion> listTransacciones = transaccionBean.findAll(); 
        if (listTransacciones == null){
            return Response.serverError().build();
        }
        return Response.ok(listTransacciones).build();
    }

    /**
     * GET /transacciones/{id} - Obtener transacción por ID
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findById(@PathParam("id") String id) {
        try {
            UUID idTransaccion = UUID.fromString(id);
            Transaccion transaccion = transaccionBean.findById(idTransaccion);
            if (transaccion == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Transacción no encontrada")
                        .build();
            }
            return Response.ok(transaccion).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID inválido")
                    .build();
        } catch (Exception ex) {
            return Response.serverError().entity(ex.getMessage()).build();
        }
    }

    /**
     * GET /transacciones/recientes?limite=10 - Obtener transacciones recientes
     */
    @GET
    @Path("recientes")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getRecientes(@QueryParam("limite") Integer limite) {
        try {
            if (limite == null || limite <= 0) {
                limite = 10; // Por defecto 10
            }
            
            List<Transaccion> transacciones = transaccionBean.findRange(0, limite);
            return Response.ok(transacciones).build();
        } catch (Exception ex) {
            return Response.serverError().entity(ex.getMessage()).build();
        }
    }

    /**
     * POST /transacciones - Crear asiento contable / transacción
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response create(Transaccion transaccion, @Context UriInfo uriInfo) {
        try {
            if (transaccion == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Datos de transacción inválidos")
                        .build();
            }

            UUID idTransaccion = UUID.randomUUID();
            transaccion.setIdTransaccion(idTransaccion);
            transaccionBean.persistEntity(transaccion);

            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(idTransaccion.toString());
            return Response.created(uriBuilder.build()).entity(transaccion).build();
        } catch (Exception ex) {
            return Response.serverError()
                    .entity("Error al crear transacción: " + ex.getMessage())
                    .build();
        }
    }
}
