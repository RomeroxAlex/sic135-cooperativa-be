package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.boundary.rest.server;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.FacturaBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.FacturaDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.ItemFacturaDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.PagedResponseDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.VentaDiariaDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.Factura;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.ItemFactura;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ResourceNotFoundException;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("facturas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Facturas", description = "Operaciones para facturación digital")
public class FacturaResource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    FacturaBean facturaBean;

    @GET
    @Operation(summary = "Listar facturas", description = "Obtiene lista paginada de facturas")
    @APIResponse(responseCode = "200", description = "Lista de facturas obtenida exitosamente")
    public Response findAll(
            @Parameter(description = "Número de página") @QueryParam("page") @DefaultValue("0") int page,
            @Parameter(description = "Tamaño de página") @QueryParam("size") @DefaultValue("20") int size) {
        
        List<Factura> facturas = facturaBean.findPaginated(page, size);
        long total = facturaBean.count();
        
        List<FacturaDto> dtos = facturas.stream()
            .map(this::toDto)
            .collect(Collectors.toList());
        
        PagedResponseDto<FacturaDto> response = new PagedResponseDto<>(dtos, page, size, total);
        return Response.ok(response).build();
    }

    @GET
    @Path("{id}")
    @Operation(summary = "Obtener factura por ID", description = "Obtiene una factura específica")
    @APIResponse(responseCode = "200", description = "Factura encontrada")
    @APIResponse(responseCode = "404", description = "Factura no encontrada")
    public Response findById(@PathParam("id") UUID id) {
        Factura factura = facturaBean.findById(id);
        if (factura == null) {
            throw new ResourceNotFoundException("Factura", id);
        }
        return Response.ok(toDto(factura)).build();
    }

    @POST
    @Operation(summary = "Crear factura", description = "Crea una nueva factura")
    @APIResponse(responseCode = "201", description = "Factura creada exitosamente")
    public Response create(@Valid FacturaDto dto, @Context UriInfo uriInfo) {
        Factura factura = toEntity(dto);
        factura = facturaBean.create(factura);
        
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path(factura.getId().toString());
        return Response.created(uriBuilder.build()).entity(toDto(factura)).build();
    }

    @PUT
    @Path("{id}")
    @Operation(summary = "Actualizar factura", description = "Actualiza una factura existente")
    @APIResponse(responseCode = "200", description = "Factura actualizada exitosamente")
    @APIResponse(responseCode = "400", description = "Factura bloqueada")
    public Response update(@PathParam("id") UUID id, @Valid FacturaDto dto) {
        Factura factura = toEntity(dto);
        factura = facturaBean.update(id, factura);
        return Response.ok(toDto(factura)).build();
    }

    @POST
    @Path("{id}/emitir")
    @Operation(summary = "Emitir factura", description = "Emite una factura")
    @APIResponse(responseCode = "200", description = "Factura emitida exitosamente")
    public Response emitir(@PathParam("id") UUID id) {
        Factura factura = facturaBean.emitir(id);
        return Response.ok(toDto(factura)).build();
    }

    @POST
    @Path("{id}/anular")
    @Operation(summary = "Anular factura", description = "Anula una factura")
    @APIResponse(responseCode = "200", description = "Factura anulada exitosamente")
    public Response anular(@PathParam("id") UUID id) {
        Factura factura = facturaBean.anular(id);
        return Response.ok(toDto(factura)).build();
    }

    private FacturaDto toDto(Factura entity) {
        FacturaDto dto = new FacturaDto();
        dto.setId(entity.getId());
        dto.setNumeroFactura(entity.getNumeroFactura());
        dto.setClienteId(entity.getClienteId());
        dto.setClienteNombre(entity.getClienteNombre());
        dto.setClienteNit(entity.getClienteNit());
        dto.setFecha(entity.getFecha());
        dto.setSubtotal(entity.getSubtotal());
        dto.setImpuestos(entity.getImpuestos());
        dto.setTotal(entity.getTotal());
        dto.setEstado(entity.getEstado());
        dto.setObservaciones(entity.getObservaciones());
        dto.setFechaCreacion(entity.getFechaCreacion());
        
        if (entity.getItems() != null) {
            List<ItemFacturaDto> items = entity.getItems().stream()
                .map(this::toItemDto)
                .collect(Collectors.toList());
            dto.setItems(items);
        }
        
        return dto;
    }

    private ItemFacturaDto toItemDto(ItemFactura entity) {
        ItemFacturaDto dto = new ItemFacturaDto();
        dto.setId(entity.getId());
        dto.setCodigoProducto(entity.getCodigoProducto());
        dto.setDescripcion(entity.getDescripcion());
        dto.setCantidad(entity.getCantidad());
        dto.setPrecioUnitario(entity.getPrecioUnitario());
        dto.setSubtotal(entity.getSubtotal());
        dto.setOrden(entity.getOrden());
        return dto;
    }

    private Factura toEntity(FacturaDto dto) {
        Factura entity = new Factura();
        entity.setClienteId(dto.getClienteId());
        entity.setClienteNombre(dto.getClienteNombre());
        entity.setClienteNit(dto.getClienteNit());
        entity.setFecha(dto.getFecha());
        entity.setObservaciones(dto.getObservaciones());
        
        if (dto.getItems() != null) {
            int orden = 1;
            for (ItemFacturaDto itemDto : dto.getItems()) {
                ItemFactura item = new ItemFactura();
                item.setCodigoProducto(itemDto.getCodigoProducto());
                item.setDescripcion(itemDto.getDescripcion());
                item.setCantidad(itemDto.getCantidad());
                item.setPrecioUnitario(itemDto.getPrecioUnitario());
                item.setOrden(orden++);
                entity.getItems().add(item);
            }
        }
        
        return entity;
    }
}
