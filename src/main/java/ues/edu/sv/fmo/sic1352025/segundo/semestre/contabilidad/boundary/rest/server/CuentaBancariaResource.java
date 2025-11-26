package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CuentaBancariaBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.TipoCuentaBancariaBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.CuentaBancariaDTO;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaBancaria;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.TipoCuentaBancaria;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.service.CuentaBancariaService;

@Path("cuentas-bancarias")
public class CuentaBancariaResource {

    @Inject
    CuentaBancariaService cuentaBancariaService;

    @Inject
    CuentaBancariaBean cuentaBancariaBean;

    @Inject
    TipoCuentaBancariaBean tipoCuentaBancariaBean;

    /**
     * GET /cuentas-bancarias - Obtener todas las cuentas bancarias
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response findAll() {
        try {
            List<CuentaBancaria> listCuentas = cuentaBancariaBean.findAll();
            if (listCuentas == null) {
                return Response.serverError().build();
            }
            return Response.ok(listCuentas).build();
        } catch (Exception ex) {
            return Response.serverError().entity(ex.getMessage()).build();
        }
    }

    /**
     * GET /cuentas-bancarias/{id} - Obtener cuenta bancaria por ID
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findById(@PathParam("id") String id) {
        try {
            UUID idCuenta = UUID.fromString(id);
            CuentaBancaria cuenta = cuentaBancariaBean.findById(idCuenta);
            if (cuenta == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Cuenta bancaria no encontrada")
                        .build();
            }
            return Response.ok(cuenta).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID inválido")
                    .build();
        } catch (Exception ex) {
            return Response.serverError().entity(ex.getMessage()).build();
        }
    }

    /**
     * GET /cuentas-bancarias/socio/{idSocio} - Obtener cuentas bancarias de un socio
     */
    @GET
    @Path("socio/{idSocio}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response findBySocio(@PathParam("idSocio") String idSocio) {
        try {
            UUID idSocioUUID = UUID.fromString(idSocio);
            List<CuentaBancaria> todasCuentas = cuentaBancariaBean.findAll();
            
            // Filtrar cuentas por socio
            List<CuentaBancaria> cuentasSocio = todasCuentas.stream()
                    .filter(c -> c.getIdSocio() != null 
                            && idSocioUUID.equals(c.getIdSocio().getIdSocio()))
                    .collect(Collectors.toList());
            
            return Response.ok(cuentasSocio).build();
        } catch (IllegalArgumentException ex) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID de socio inválido")
                    .build();
        } catch (Exception ex) {
            return Response.serverError().entity(ex.getMessage()).build();
        }
    }

    /**
     * GET /cuentas-bancarias/tipos - Obtener tipos de cuentas bancarias
     */
    @GET
    @Path("tipos")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getTiposCuentaBancaria() {
        try {
            List<TipoCuentaBancaria> tipos = tipoCuentaBancariaBean.findAll();
            if (tipos == null) {
                return Response.serverError().build();
            }
            return Response.ok(tipos).build();
        } catch (Exception ex) {
            return Response.serverError().entity(ex.getMessage()).build();
        }
    }

    /**
     * POST-APERTURA_CUENTA
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

    /*
    * APORTE DE CUENTA PUT 
    */
   @PUT
   @Path("/aporte")
   @Consumes({MediaType.APPLICATION_JSON})
   @Produces({MediaType.APPLICATION_JSON})
   public Response aporteCuentaBancaria(CuentaBancariaDTO cuentaBancariaDTO){
        try {
            cuentaBancariaService.aporteCuenta(cuentaBancariaDTO);
            return Response.noContent().build();
        } catch (Exception e) {
            // TODO: handle exception
            return Response.serverError().build();
        }
    }
    
    @PUT
    @Path("/retiro")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public Response retiroCuentaBancaria(CuentaBancariaDTO cuentaBancariaDTO){
        try {
            cuentaBancariaService.retiroCuenta(cuentaBancariaDTO);
            return Response.noContent().build();
        } catch (Exception e) {
            // TODO: handle exception
            return Response.serverError().build();
        }
    }

}
