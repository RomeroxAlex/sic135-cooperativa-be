package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import java.util.UUID;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.CuentaBancariaDTO;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.service.CuentaBancariaService;

@Path("cuentas-bancarias")
public class CuentaBancariaResource {

    @Inject
    CuentaBancariaService cuentaBancariaService;

    /*
        OBTENER CUENTA BANCARIA DE UN SOCIO
    */



    /*
        APERTURA_CUENTA
    */
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response crearCuentaBancaria(CuentaBancariaDTO cuentaBancariaDTO, @Context UriInfo uriInfo){
        try{
            UUID idCuentaBancaria = cuentaBancariaService.crearCuentaBancaria(cuentaBancariaDTO);
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(idCuentaBancaria.toString());
            return Response.created(uriBuilder.build()).build();
        }catch(Exception ex){
            return Response.serverError().build();
        }

    }
    

}
