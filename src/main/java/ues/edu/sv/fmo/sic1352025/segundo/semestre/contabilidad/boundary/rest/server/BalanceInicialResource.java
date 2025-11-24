package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.BalanceInicialBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CatalogoCuentaBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.BalanceInicialDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.BalanceInicial;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CatalogoCuenta;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ResourceNotFoundException;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Path("balance-inicial")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Balance Inicial", description = "Operaciones para el balance inicial")
public class BalanceInicialResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    BalanceInicialBean balanceInicialBean;

    @Inject
    CatalogoCuentaBean catalogoCuentaBean;

    @GET
    @Path("{periodo}")
    @Operation(summary = "Obtener balance inicial", description = "Obtiene el balance inicial de un periodo")
    @APIResponse(responseCode = "200", description = "Balance inicial obtenido exitosamente")
    public Response getByPeriodo(@Parameter(description = "Periodo (YYYY)") @PathParam("periodo") String periodo) {
        List<BalanceInicial> balances = balanceInicialBean.findByPeriodo(periodo);
        List<BalanceInicialDto> dtos = balances.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
        return Response.ok(dtos).build();
    }

    @POST
    @Operation(summary = "Crear balance inicial", description = "Crea el balance inicial para un periodo")
    @APIResponse(responseCode = "201", description = "Balance inicial creado exitosamente")
    @APIResponse(responseCode = "400", description = "Balance no balanceado o datos inválidos")
    public Response create(
            @Parameter(description = "Periodo (YYYY)") @QueryParam("periodo") String periodo,
            @Valid List<BalanceInicialDto> dtos, 
            @Context UriInfo uriInfo) {
        
        List<BalanceInicial> balances = dtos.stream()
            .map(this::toEntity)
            .collect(Collectors.toList());
        
        balances = balanceInicialBean.crearBalanceInicial(periodo, balances);
        
        List<BalanceInicialDto> resultDtos = balances.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
        
        return Response.status(Response.Status.CREATED).entity(resultDtos).build();
    }

    private BalanceInicialDto toDto(BalanceInicial entity) {
        BalanceInicialDto dto = new BalanceInicialDto();
        dto.setId(entity.getId());
        dto.setPeriodo(entity.getPeriodo());
        dto.setCuentaId(entity.getCuenta().getId());
        dto.setCuentaCodigo(entity.getCuenta().getCodigo());
        dto.setCuentaNombre(entity.getCuenta().getNombre());
        dto.setSaldoDebe(entity.getSaldoDebe());
        dto.setSaldoHaber(entity.getSaldoHaber());
        dto.setFechaRegistro(entity.getFechaRegistro());
        return dto;
    }

    private BalanceInicial toEntity(BalanceInicialDto dto) {
        BalanceInicial entity = new BalanceInicial();
        entity.setPeriodo(dto.getPeriodo());
        
        CatalogoCuenta cuenta = catalogoCuentaBean.findById(dto.getCuentaId());
        if (cuenta == null) {
            throw new ResourceNotFoundException("CatalogoCuenta", dto.getCuentaId());
        }
        entity.setCuenta(cuenta);
        entity.setSaldoDebe(dto.getSaldoDebe());
        entity.setSaldoHaber(dto.getSaldoHaber());
        entity.setFechaRegistro(dto.getFechaRegistro());
        return entity;
    }
}
