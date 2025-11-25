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
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.PagedResponseDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.PartidaDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CatalogoCuenta;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.LineaPartida;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PartidaDiario;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ResourceNotFoundException;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("diario/partidas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Partidas de Diario", description = "Operaciones para el libro diario contable")
public class PartidaDiarioResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    PartidaDiarioBean partidaDiarioBean;

    @Inject
    CatalogoCuentaBean catalogoCuentaBean;

    @GET
    @Operation(summary = "Listar partidas", description = "Obtiene lista paginada de partidas de diario")
    @APIResponse(responseCode = "200", description = "Lista de partidas obtenida exitosamente")
    public Response findAll(
            @Parameter(description = "Número de página") @QueryParam("page") @DefaultValue("0") int page,
            @Parameter(description = "Tamaño de página") @QueryParam("size") @DefaultValue("20") int size,
            @Parameter(description = "Filtro por estado") @QueryParam("estado") String estado,
            @Parameter(description = "Fecha inicio") @QueryParam("fechaInicio") String fechaInicio,
            @Parameter(description = "Fecha fin") @QueryParam("fechaFin") String fechaFin) {
        
        LocalDate inicio = fechaInicio != null ? LocalDate.parse(fechaInicio) : null;
        LocalDate fin = fechaFin != null ? LocalDate.parse(fechaFin) : null;
        
        List<PartidaDiario> partidas = partidaDiarioBean.findPaginated(page, size, estado, inicio, fin);
        long total = partidaDiarioBean.count();
        
        List<PartidaDto> dtos = partidas.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
        
        PagedResponseDto<PartidaDto> response = new PagedResponseDto<>(dtos, page, size, total);
        return Response.ok(response).build();
    }

    @GET
    @Path("{id}")
    @Operation(summary = "Obtener partida por ID", description = "Obtiene una partida específica con sus líneas")
    @APIResponse(responseCode = "200", description = "Partida encontrada")
    @APIResponse(responseCode = "404", description = "Partida no encontrada")
    public Response findById(@PathParam("id") UUID id) {
        PartidaDiario partida = partidaDiarioBean.findById(id);
        if (partida == null) {
            throw new ResourceNotFoundException("PartidaDiario", id);
        }
        return Response.ok(toDto(partida)).build();
    }

    @POST
    @Operation(summary = "Crear partida", description = "Crea una nueva partida de diario")
    @APIResponse(responseCode = "201", description = "Partida creada exitosamente")
    @APIResponse(responseCode = "400", description = "Partida no balanceada o datos inválidos")
    public Response create(@Valid PartidaDto dto, @Context UriInfo uriInfo) {
        PartidaDiario partida = toEntity(dto);
        partida = partidaDiarioBean.create(partida);
        
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(partida.getId().toString());
        return Response.created(uriBuilder.build()).entity(toDto(partida)).build();
    }

    @PUT
    @Path("{id}")
    @Operation(summary = "Actualizar partida", description = "Actualiza una partida existente (solo si está en borrador)")
    @APIResponse(responseCode = "200", description = "Partida actualizada exitosamente")
    @APIResponse(responseCode = "400", description = "Partida bloqueada o no balanceada")
    @APIResponse(responseCode = "404", description = "Partida no encontrada")
    public Response update(@PathParam("id") UUID id, @Valid PartidaDto dto) {
        PartidaDiario partida = toEntity(dto);
        partida = partidaDiarioBean.update(id, partida);
        return Response.ok(toDto(partida)).build();
    }

    @POST
    @Path("{id}/postear")
    @Operation(summary = "Postear partida", description = "Cambia el estado de la partida a POSTEADA")
    @APIResponse(responseCode = "200", description = "Partida posteada exitosamente")
    @APIResponse(responseCode = "400", description = "No se puede postear la partida")
    public Response postear(@PathParam("id") UUID id) {
        PartidaDiario partida = partidaDiarioBean.postear(id);
        return Response.ok(toDto(partida)).build();
    }

    @POST
    @Path("{id}/anular")
    @Operation(summary = "Anular partida", description = "Anula una partida de diario")
    @APIResponse(responseCode = "200", description = "Partida anulada exitosamente")
    public Response anular(@PathParam("id") UUID id) {
        PartidaDiario partida = partidaDiarioBean.anular(id);
        return Response.ok(toDto(partida)).build();
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
        entity.setEsAjuste(dto.getEsAjuste() != null ? dto.getEsAjuste() : false);
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
