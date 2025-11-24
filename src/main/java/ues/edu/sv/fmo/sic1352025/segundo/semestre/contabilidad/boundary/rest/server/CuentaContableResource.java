package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CuentaContableBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaContable;

/**
 *
 * @author jordi
 */
@Path("cuentas-contables")
public class CuentaContableResource implements Serializable{
    
    @Inject
    CuentaContableBean cuentaContableBean;
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response findAll(){
        List<CuentaContable> listCuentasContables = cuentaContableBean.findAll();
        if(listCuentasContables == null){
            return Response.serverError().build();
        }
        return Response.ok(listCuentasContables).build();
    }
    
}
