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
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.CreditoDTO;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.service.CreditoService;

@Path("creditos")
public class CreditoResource {

    @Inject
    CreditoService creditoService;

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response crearCredito(CreditoDTO creditoDTO, @Context UriInfo uriInfo){
        // CREAR Uri
        try {
            /*  LOGICA DE PERSISTENCIA DEL CRÉDITO */
            UUID idCredito = creditoService.serviceCrearCredito(creditoDTO);
            /* LOGICA DE CREACION DE ASIENTO CONTABLE */
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(idCredito.toString());
            return Response.ok(uriBuilder.build()).build();
        } catch (Exception ex) {
            return Response.serverError().build();
        }
    }


}
