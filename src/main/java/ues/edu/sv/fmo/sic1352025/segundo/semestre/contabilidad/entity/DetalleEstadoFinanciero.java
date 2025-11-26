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
 * Entity representing a detail line of a financial statement.
 */
@Entity
@Table(name = "detalle_estado_financiero")
@NamedQueries({
    @NamedQuery(name = "DetalleEstadoFinanciero.findAll", query = "SELECT d FROM DetalleEstadoFinanciero d"),
    @NamedQuery(name = "DetalleEstadoFinanciero.findByEstado", query = "SELECT d FROM DetalleEstadoFinanciero d WHERE d.idEstadoFinanciero = :estado ORDER BY d.orden"),
    @NamedQuery(name = "DetalleEstadoFinanciero.findByCategoria", query = "SELECT d FROM DetalleEstadoFinanciero d WHERE d.categoria = :categoria")
})
public class DetalleEstadoFinanciero implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "id_detalle_estado")
    private UUID idDetalleEstado;
    
    @Column(name = "categoria")
    private String categoria; // ACTIVO_CORRIENTE, ACTIVO_NO_CORRIENTE, PASIVO_CORRIENTE, etc.
    
    @Column(name = "subcategoria")
    private String subcategoria;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "monto")
    private BigDecimal monto;
    
    @Column(name = "orden")
    private Integer orden;
    
    @JoinColumn(name = "id_estado_financiero", referencedColumnName = "id_estado_financiero")
    @ManyToOne(fetch = FetchType.LAZY)
    private EstadoFinanciero idEstadoFinanciero;
    
    @JoinColumn(name = "id_cuenta_contable", referencedColumnName = "id_cuenta_contable")
    @ManyToOne(fetch = FetchType.LAZY)
    private CuentaContable idCuentaContable;

    public DetalleEstadoFinanciero() {
    }

    public DetalleEstadoFinanciero(UUID idDetalleEstado) {
        this.idDetalleEstado = idDetalleEstado;
    }

    public UUID getIdDetalleEstado() {
        return idDetalleEstado;
    }

    public void setIdDetalleEstado(UUID idDetalleEstado) {
        this.idDetalleEstado = idDetalleEstado;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(String subcategoria) {
        this.subcategoria = subcategoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    @JsonbTransient
    public EstadoFinanciero getIdEstadoFinanciero() {
        return idEstadoFinanciero;
    }

    public void setIdEstadoFinanciero(EstadoFinanciero idEstadoFinanciero) {
        this.idEstadoFinanciero = idEstadoFinanciero;
    }

    public CuentaContable getIdCuentaContable() {
        return idCuentaContable;
    }

    public void setIdCuentaContable(CuentaContable idCuentaContable) {
        this.idCuentaContable = idCuentaContable;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDetalleEstado != null ? idDetalleEstado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DetalleEstadoFinanciero)) {
            return false;
        }
        DetalleEstadoFinanciero other = (DetalleEstadoFinanciero) object;
        return !((this.idDetalleEstado == null && other.idDetalleEstado != null) 
                || (this.idDetalleEstado != null && !this.idDetalleEstado.equals(other.idDetalleEstado)));
    }

    @Override
    public String toString() {
        return "DetalleEstadoFinanciero[ idDetalleEstado=" + idDetalleEstado + " ]";
    }
}
