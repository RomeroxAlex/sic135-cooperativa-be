package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CatalogoCuentaBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.PartidaDiarioBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.LineaDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.PartidaDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CatalogoCuenta;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.LineaPartida;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PartidaDiario;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ResourceNotFoundException;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("ajustes/partidas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Partidas de Ajuste", description = "Operaciones para partidas de ajuste contable")
public class PartidaAjusteResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    PartidaDiarioBean partidaDiarioBean;

    @Inject
    CatalogoCuentaBean catalogoCuentaBean;

    @GET
    @Operation(summary = "Listar partidas de ajuste", description = "Obtiene todas las partidas de ajuste")
    @APIResponse(responseCode = "200", description = "Lista de ajustes obtenida exitosamente")
    public Response findAll() {
        List<PartidaDiario> partidas = partidaDiarioBean.findAjustes();
        List<PartidaDto> dtos = partidas.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
        return Response.ok(dtos).build();
    }

    @POST
    @Operation(summary = "Crear partida de ajuste", description = "Crea una nueva partida de ajuste")
    @APIResponse(responseCode = "201", description = "Partida de ajuste creada exitosamente")
    @APIResponse(responseCode = "400", description = "Partida no balanceada o datos inválidos")
    public Response create(@Valid PartidaDto dto, @Context UriInfo uriInfo) {
        PartidaDiario partida = toEntity(dto);
        partida.setEsAjuste(true);
        partida = partidaDiarioBean.create(partida);
        
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(partida.getId().toString());
        return Response.created(uriBuilder.build()).entity(toDto(partida)).build();
    }

    private PartidaDto toDto(PartidaDiario entity) {
        PartidaDto dto = new PartidaDto();
        dto.setId(entity.getId());
        dto.setNumeroPartida(entity.getNumeroPartida());
        dto.setFecha(entity.getFecha());
        dto.setDescripcion(entity.getDescripcion());
        dto.setReferencia(entity.getReferencia());
        dto.setEstado(entity.getEstado());
        dto.setEsAjuste(entity.getEsAjuste());
        dto.setMotivoAjuste(entity.getMotivoAjuste());
        dto.setTotalDebe(entity.getTotalDebe());
        dto.setTotalHaber(entity.getTotalHaber());
        dto.setFechaCreacion(entity.getFechaCreacion());
        
        if (entity.getLineas() != null) {
            List<LineaDto> lineas = entity.getLineas().stream()
                .map(this::toLineaDto)
                .collect(Collectors.toList());
            dto.setLineas(lineas);
        }
        
        return dto;
    }

    private LineaDto toLineaDto(LineaPartida entity) {
        LineaDto dto = new LineaDto();
        dto.setId(entity.getId());
        dto.setCuentaId(entity.getCuenta().getId());
        dto.setCuentaCodigo(entity.getCuenta().getCodigo());
        dto.setCuentaNombre(entity.getCuenta().getNombre());
        dto.setDescripcion(entity.getDescripcion());
        dto.setDebe(entity.getDebe());
        dto.setHaber(entity.getHaber());
        dto.setOrden(entity.getOrden());
        return dto;
    }

    private PartidaDiario toEntity(PartidaDto dto) {
        PartidaDiario entity = new PartidaDiario();
        entity.setFecha(dto.getFecha());
        entity.setDescripcion(dto.getDescripcion());
        entity.setReferencia(dto.getReferencia());
        entity.setEsAjuste(true);
        entity.setMotivoAjuste(dto.getMotivoAjuste());
        
        if (dto.getLineas() != null) {
            int orden = 1;
            for (LineaDto lineaDto : dto.getLineas()) {
                LineaPartida linea = new LineaPartida();
                CatalogoCuenta cuenta = catalogoCuentaBean.findById(lineaDto.getCuentaId());
                if (cuenta == null) {
                    throw new ResourceNotFoundException("CatalogoCuenta", lineaDto.getCuentaId());
                }
                linea.setCuenta(cuenta);
                linea.setDescripcion(lineaDto.getDescripcion());
                linea.setDebe(lineaDto.getDebe());
                linea.setHaber(lineaDto.getHaber());
                linea.setOrden(orden++);
                entity.getLineas().add(linea);
            }
        }
        
        return entity;
    }
}
