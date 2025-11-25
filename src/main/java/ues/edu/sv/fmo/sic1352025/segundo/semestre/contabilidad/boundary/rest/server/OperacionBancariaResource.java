package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.OperacionBancariaBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.OperacionBancaria;


@Path("operaciones-bancarias")
public class OperacionBancariaResource {
    
    @Inject
    OperacionBancariaBean operacionBancariaBean;

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
     * POST PARA OPERACIONES ; DEBEN PASAR VALIDACIONES
     */
}
