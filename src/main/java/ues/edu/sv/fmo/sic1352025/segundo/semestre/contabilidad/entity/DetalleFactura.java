package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * Entity representing a detail line of an invoice.
 */
@Entity
@Table(name = "detalle_factura")
@NamedQueries({
    @NamedQuery(name = "DetalleFactura.findAll", query = "SELECT d FROM DetalleFactura d"),
    @NamedQuery(name = "DetalleFactura.findByFactura", query = "SELECT d FROM DetalleFactura d WHERE d.idFactura = :factura ORDER BY d.numeroLinea")
})
public class DetalleFactura implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "id_detalle_factura")
    private UUID idDetalleFactura;
    
    @Column(name = "numero_linea")
    private Integer numeroLinea;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "cantidad")
    private BigDecimal cantidad;
    
    @Column(name = "precio_unitario")
    private BigDecimal precioUnitario;
    
    @Column(name = "subtotal")
    private BigDecimal subtotal;
    
    @JoinColumn(name = "id_factura", referencedColumnName = "id_factura")
    @ManyToOne(fetch = FetchType.LAZY)
    private Factura idFactura;

    public DetalleFactura() {
    }

    public DetalleFactura(UUID idDetalleFactura) {
        this.idDetalleFactura = idDetalleFactura;
    }

    public UUID getIdDetalleFactura() {
        return idDetalleFactura;
    }

    public void setIdDetalleFactura(UUID idDetalleFactura) {
        this.idDetalleFactura = idDetalleFactura;
    }

    public Integer getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(Integer numeroLinea) {
        this.numeroLinea = numeroLinea;
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

    @JsonbTransient
    public Factura getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Factura idFactura) {
        this.idFactura = idFactura;
    }
    
    /**
     * Calculate subtotal from quantity and unit price
     */
    public void calcularSubtotal() {
        if (cantidad != null && precioUnitario != null) {
            this.subtotal = cantidad.multiply(precioUnitario);
        } else {
            this.subtotal = BigDecimal.ZERO;
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalleFactura != null ? idDetalleFactura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DetalleFactura)) {
            return false;
        }
        DetalleFactura other = (DetalleFactura) object;
        return !((this.idDetalleFactura == null && other.idDetalleFactura != null) 
                || (this.idDetalleFactura != null && !this.idDetalleFactura.equals(other.idDetalleFactura)));
    }

    @Override
    public String toString() {
        return "DetalleFactura[ idDetalleFactura=" + idDetalleFactura + " ]";
    }
}
