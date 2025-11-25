package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.TipoCuentaContableBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.TipoCuentaContable;


@Path("tipo-cuentas-contables")
public class TipoCuentaContableResource implements Serializable {

    @Inject
    TipoCuentaContableBean tipoCuentaContableBean;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response findAll(){
        List<TipoCuentaContable> listTipoCuentaContables = tipoCuentaContableBean.findAll();
        if(listTipoCuentaContables == null){
            return Response.serverError().build();
        }
        return Response.ok(listTipoCuentaContables).build();
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response crearCuentaContable (TipoCuentaContable tipoCuentaContable, @Context UriInfo uriInfo){
        try {
            UUID idCuentaContable = UUID.randomUUID();
                                 
            tipoCuentaContable.setIdTipoCuentaContable(idCuentaContable);
                                    System.out.println("Aqui 3");

            tipoCuentaContableBean.persistEntity(tipoCuentaContable);
                                    System.out.println("Aqui 2");

            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(idCuentaContable.toString());
            return Response.created(uriBuilder.build()).build();

        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            //return Response.serverError().build();
            return Response.status(414).build();
        }
    }
}
