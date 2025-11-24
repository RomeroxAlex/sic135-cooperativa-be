package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FacturaDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private String numeroFactura;

    private UUID clienteId;

    @Size(max = 200, message = "El nombre del cliente no puede exceder 200 caracteres")
    private String clienteNombre;

    @Size(max = 20, message = "El NIT no puede exceder 20 caracteres")
    private String clienteNit;

    @NotNull(message = "La fecha es requerida")
    private LocalDate fecha;

    @Valid
    @NotNull(message = "Los items son requeridos")
    @Size(min = 1, message = "Debe tener al menos un item")
    private List<ItemFacturaDto> items = new ArrayList<>();

    private BigDecimal subtotal;

    private BigDecimal impuestos;

    private BigDecimal total;

    private String estado;

    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;

    private LocalDateTime fechaCreacion;

    public FacturaDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public UUID getClienteId() {
        return clienteId;
    }

    public void setClienteId(UUID clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getClienteNit() {
        return clienteNit;
    }

    public void setClienteNit(String clienteNit) {
        this.clienteNit = clienteNit;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public List<ItemFacturaDto> getItems() {
        return items;
    }

    public void setItems(List<ItemFacturaDto> items) {
        this.items = items;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(BigDecimal impuestos) {
        this.impuestos = impuestos;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
