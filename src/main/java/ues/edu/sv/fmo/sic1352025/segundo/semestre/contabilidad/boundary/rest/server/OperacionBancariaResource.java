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
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.OperacionBancariaBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.OperacionBancaria;


@Path("operaciones-bancarias")
public class OperacionBancariaResource {
    
    @Inject
    OperacionBancariaBean operacionBancariaBean;

    /**
     * GET /operaciones-bancarias - Obtener todas las operaciones bancarias
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response findAll(){
        try{
            List<OperacionBancaria> listOperacionesBancarias = operacionBancariaBean.findAll();
            return Response.ok(listOperacionesBancarias).build();
        }catch (Exception ex){
            return Response.serverError().build();
        }
    }

    /**
     * GET /operaciones-bancarias/{id} - Obtener operación bancaria por ID
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findById(@PathParam("id") String id) {
        try {
            UUID idOperacion = UUID.fromString(id);
            OperacionBancaria operacion = operacionBancariaBean.findById(idOperacion);
            if (operacion == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Operación bancaria no encontrada")
                        .build();
            }
            return Response.ok(operacion).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID inválido")
                    .build();
        } catch (Exception ex) {
            return Response.serverError().entity(ex.getMessage()).build();
        }
    }

    /**
     * GET /operaciones-bancarias/cuenta/{idCuenta} - Obtener operaciones de una cuenta bancaria
     * Nota: Esta es una aproximación, ya que OperacionBancaria no tiene referencia directa a CuentaBancaria
     */
    @GET
    @Path("cuenta/{idCuenta}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findByCuenta(@PathParam("idCuenta") String idCuenta) {
        try {
            // Por ahora retornamos todas las operaciones
            // En una implementación completa, necesitaríamos la relación con CuentaBancaria
            List<OperacionBancaria> operaciones = operacionBancariaBean.findAll();
            return Response.ok(operaciones).build();
        } catch (Exception ex) {
            return Response.serverError().entity(ex.getMessage()).build();
        }
    }

    /**
     * POST /operaciones-bancarias - Crear operación bancaria (depósito/retiro)
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response create(OperacionBancaria operacion, @Context UriInfo uriInfo) {
        try {
            if (operacion == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Datos de operación inválidos")
                        .build();
            }

            UUID idOperacion = UUID.randomUUID();
            operacion.setIdOperacionBancaria(idOperacion);
            operacionBancariaBean.persistEntity(operacion);

            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(idOperacion.toString());
            return Response.created(uriBuilder.build()).entity(operacion).build();
        } catch (Exception ex) {
            return Response.serverError()
                    .entity("Error al crear operación: " + ex.getMessage())
                    .build();
        }
    }
}
