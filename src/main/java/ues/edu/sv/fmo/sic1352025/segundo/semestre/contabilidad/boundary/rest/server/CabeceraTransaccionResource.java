package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CabeceraTransaccionBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CabeceraTransaccion;

@Path("cabeceras-transacciones")
public class CabeceraTransaccionResource {

    @Inject
    CabeceraTransaccionBean cabeceraTransaccionBean;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response findAll(){
        try {
            List<CabeceraTransaccion> listCabeceraTransaccion = cabeceraTransaccionBean.findAll();
            return Response.ok(listCabeceraTransaccion).build();
        } catch (Exception e) {
            // TODO: handle exception
            return Response.serverError().build();
        }
    }

}
