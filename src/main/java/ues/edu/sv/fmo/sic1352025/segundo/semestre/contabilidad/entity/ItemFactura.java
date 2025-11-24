package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "item_factura")
@NamedQueries({
    @NamedQuery(name = "ItemFactura.findByFactura", query = "SELECT i FROM ItemFactura i WHERE i.factura.id = :facturaId")
})
public class ItemFactura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_item_factura", columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_factura", nullable = false)
    private Factura factura;

    @Column(name = "codigo_producto", length = 50)
    private String codigoProducto;

    @Column(name = "descripcion", nullable = false, length = 300)
    private String descripcion;

    @Column(name = "cantidad", precision = 18, scale = 4)
    private BigDecimal cantidad = BigDecimal.ONE;

    @Column(name = "precio_unitario", precision = 18, scale = 2)
    private BigDecimal precioUnitario = BigDecimal.ZERO;

    @Column(name = "subtotal", precision = 18, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "orden")
    private Integer orden;

    public ItemFactura() {
    }

    public ItemFactura(UUID id) {
        this.id = id;
    }

    @PrePersist
    @PreUpdate
    protected void calcularSubtotal() {
        if (cantidad != null && precioUnitario != null) {
            this.subtotal = cantidad.multiply(precioUnitario);
        }
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
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

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ItemFactura that = (ItemFactura) obj;
        return id != null && id.equals(that.id);
    }

    @Override
    public String toString() {
        return "ItemFactura{id=" + id + ", descripcion='" + descripcion + "', subtotal=" + subtotal + "}";
    }
}
