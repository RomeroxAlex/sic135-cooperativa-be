package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

public class ItemFacturaDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @Size(max = 50, message = "El código de producto no puede exceder 50 caracteres")
    private String codigoProducto;

    @NotBlank(message = "La descripción es requerida")
    @Size(max = 300, message = "La descripción no puede exceder 300 caracteres")
    private String descripcion;

    @NotNull(message = "La cantidad es requerida")
    private BigDecimal cantidad;

    @NotNull(message = "El precio unitario es requerido")
    private BigDecimal precioUnitario;

    private BigDecimal subtotal;

    private Integer orden;

    public ItemFacturaDto() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }
}
