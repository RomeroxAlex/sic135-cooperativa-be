package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.AsientoContableBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.AsientoContable;

@Path("asientos")
public class AsientoResource {

    @Inject
    AsientoContableBean asientoContableBean;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response findAll(){
        try {
            List<AsientoContable> listAsiento = asientoContableBean.findAll();
            return Response.ok(listAsiento).build();
        } catch (Exception e) {
            // TODO: handle exception
            return Response.serverError().build();
        }
    }
}
