package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import java.io.Serializable;
import java.util.List;

import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.TransaccionBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.Transaccion;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("transacciones")
public class TransaccionResource implements Serializable{

    @Inject
    TransaccionBean transaccionBean;

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
     * METHOD POST ASIENTOS CONTABLES
     */
    


}
