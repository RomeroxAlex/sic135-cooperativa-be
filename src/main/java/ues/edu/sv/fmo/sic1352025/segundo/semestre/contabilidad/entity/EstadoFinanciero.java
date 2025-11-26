package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * Entity representing a financial statement (Estado Financiero).
 * Types: BALANCE_GENERAL, ESTADO_RESULTADOS
 */
@Entity
@Table(name = "estado_financiero")
@NamedQueries({
    @NamedQuery(name = "EstadoFinanciero.findAll", query = "SELECT e FROM EstadoFinanciero e ORDER BY e.fechaGeneracion DESC"),
    @NamedQuery(name = "EstadoFinanciero.findByPeriodo", query = "SELECT e FROM EstadoFinanciero e WHERE e.idPeriodoContable = :periodo"),
    @NamedQuery(name = "EstadoFinanciero.findByTipo", query = "SELECT e FROM EstadoFinanciero e WHERE e.tipoEstado = :tipo"),
    @NamedQuery(name = "EstadoFinanciero.findByPeriodoYTipo", query = "SELECT e FROM EstadoFinanciero e WHERE e.idPeriodoContable = :periodo AND e.tipoEstado = :tipo ORDER BY e.fechaGeneracion DESC")
})
public class EstadoFinanciero implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "id_estado_financiero")
    private UUID idEstadoFinanciero;
    
    @Column(name = "tipo_estado")
    private String tipoEstado; // BALANCE_GENERAL, ESTADO_RESULTADOS
    
    @Column(name = "fecha_generacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaGeneracion;
    
    @Column(name = "fecha_corte")
    @Temporal(TemporalType.DATE)
    private Date fechaCorte;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "total_activos")
    private BigDecimal totalActivos;
    
    @Column(name = "total_pasivos")
    private BigDecimal totalPasivos;
    
    @Column(name = "total_patrimonio")
    private BigDecimal totalPatrimonio;
    
    @Column(name = "total_ingresos")
    private BigDecimal totalIngresos;
    
    @Column(name = "total_gastos")
    private BigDecimal totalGastos;
    
    @Column(name = "utilidad_neta")
    private BigDecimal utilidadNeta;
    
    @Column(name = "estado")
    private String estado; // BORRADOR, DEFINITIVO
    
    @JoinColumn(name = "id_periodo_contable", referencedColumnName = "id_periodo_contable")
    @ManyToOne(fetch = FetchType.LAZY)
    private PeriodoContable idPeriodoContable;
    
    @OneToMany(mappedBy = "idEstadoFinanciero", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleEstadoFinanciero> detalles;

    public EstadoFinanciero() {
    }

    public EstadoFinanciero(UUID idEstadoFinanciero) {
        this.idEstadoFinanciero = idEstadoFinanciero;
    }

    public UUID getIdEstadoFinanciero() {
        return idEstadoFinanciero;
    }

    public void setIdEstadoFinanciero(UUID idEstadoFinanciero) {
        this.idEstadoFinanciero = idEstadoFinanciero;
    }

    public String getTipoEstado() {
        return tipoEstado;
    }

    public void setTipoEstado(String tipoEstado) {
        this.tipoEstado = tipoEstado;
    }

    public Date getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(Date fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public Date getFechaCorte() {
        return fechaCorte;
    }

    public void setFechaCorte(Date fechaCorte) {
        this.fechaCorte = fechaCorte;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getTotalActivos() {
        return totalActivos;
    }

    public void setTotalActivos(BigDecimal totalActivos) {
        this.totalActivos = totalActivos;
    }

    public BigDecimal getTotalPasivos() {
        return totalPasivos;
    }

    public void setTotalPasivos(BigDecimal totalPasivos) {
        this.totalPasivos = totalPasivos;
    }

    public BigDecimal getTotalPatrimonio() {
        return totalPatrimonio;
    }

    public void setTotalPatrimonio(BigDecimal totalPatrimonio) {
        this.totalPatrimonio = totalPatrimonio;
    }

    public BigDecimal getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(BigDecimal totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    public BigDecimal getTotalGastos() {
        return totalGastos;
    }

    public void setTotalGastos(BigDecimal totalGastos) {
        this.totalGastos = totalGastos;
    }

    public BigDecimal getUtilidadNeta() {
        return utilidadNeta;
    }

    public void setUtilidadNeta(BigDecimal utilidadNeta) {
        this.utilidadNeta = utilidadNeta;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @JsonbTransient
    public PeriodoContable getIdPeriodoContable() {
        return idPeriodoContable;
    }

    public void setIdPeriodoContable(PeriodoContable idPeriodoContable) {
        this.idPeriodoContable = idPeriodoContable;
    }

    public List<DetalleEstadoFinanciero> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleEstadoFinanciero> detalles) {
        this.detalles = detalles;
    }
    
    /**
     * Validates that the balance sheet equation holds: Activos = Pasivos + Patrimonio
     * @return true if balanced
     */
    public boolean balanceSheetEquationHolds() {
        if (totalActivos == null || totalPasivos == null || totalPatrimonio == null) {
            return false;
        }
        return totalActivos.compareTo(totalPasivos.add(totalPatrimonio)) == 0;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEstadoFinanciero != null ? idEstadoFinanciero.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof EstadoFinanciero)) {
            return false;
        }
        EstadoFinanciero other = (EstadoFinanciero) object;
        return !((this.idEstadoFinanciero == null && other.idEstadoFinanciero != null) 
                || (this.idEstadoFinanciero != null && !this.idEstadoFinanciero.equals(other.idEstadoFinanciero)));
    }

    @Override
    public String toString() {
        return "EstadoFinanciero[ idEstadoFinanciero=" + idEstadoFinanciero + " ]";
    }
}
