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
 * Entity representing a journal entry (Partida de Diario) in the accounting system.
 * Each entry must balance: total debit = total credit
 */
@Entity
@Table(name = "partida_diario")
@NamedQueries({
    @NamedQuery(name = "PartidaDiario.findAll", query = "SELECT p FROM PartidaDiario p ORDER BY p.fecha DESC, p.numeroPartida DESC"),
    @NamedQuery(name = "PartidaDiario.findByPeriodo", query = "SELECT p FROM PartidaDiario p WHERE p.idPeriodoContable = :periodo ORDER BY p.numeroPartida"),
    @NamedQuery(name = "PartidaDiario.findByFecha", query = "SELECT p FROM PartidaDiario p WHERE p.fecha = :fecha"),
    @NamedQuery(name = "PartidaDiario.findByRangoFechas", query = "SELECT p FROM PartidaDiario p WHERE p.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY p.fecha, p.numeroPartida"),
    @NamedQuery(name = "PartidaDiario.findByTipo", query = "SELECT p FROM PartidaDiario p WHERE p.tipoPartida = :tipo"),
    @NamedQuery(name = "PartidaDiario.findMaxNumeroByPeriodo", query = "SELECT MAX(p.numeroPartida) FROM PartidaDiario p WHERE p.idPeriodoContable = :periodo"),
    @NamedQuery(name = "PartidaDiario.findByEstado", query = "SELECT p FROM PartidaDiario p WHERE p.estado = :estado")
})
public class PartidaDiario implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "id_partida_diario")
    private UUID idPartidaDiario;
    
    @Column(name = "numero_partida")
    private Integer numeroPartida;
    
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "concepto")
    private String concepto;
    
    @Column(name = "total_debe")
    private BigDecimal totalDebe;
    
    @Column(name = "total_haber")
    private BigDecimal totalHaber;
    
    @Column(name = "tipo_partida")
    private String tipoPartida; // NORMAL, AJUSTE, CIERRE, APERTURA
    
    @Column(name = "estado")
    private String estado; // BORRADOR, CONTABILIZADA, ANULADA
    
    @Column(name = "referencia")
    private String referencia;
    
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    
    @Column(name = "fecha_contabilizacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaContabilizacion;
    
    @JoinColumn(name = "id_periodo_contable", referencedColumnName = "id_periodo_contable")
    @ManyToOne(fetch = FetchType.LAZY)
    private PeriodoContable idPeriodoContable;
    
    @OneToMany(mappedBy = "idPartidaDiario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetallePartidaDiario> detalles;

    public PartidaDiario() {
    }

    public PartidaDiario(UUID idPartidaDiario) {
        this.idPartidaDiario = idPartidaDiario;
    }

    public UUID getIdPartidaDiario() {
        return idPartidaDiario;
    }

    public void setIdPartidaDiario(UUID idPartidaDiario) {
        this.idPartidaDiario = idPartidaDiario;
    }

    public Integer getNumeroPartida() {
        return numeroPartida;
    }

    public void setNumeroPartida(Integer numeroPartida) {
        this.numeroPartida = numeroPartida;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public BigDecimal getTotalDebe() {
        return totalDebe;
    }

    public void setTotalDebe(BigDecimal totalDebe) {
        this.totalDebe = totalDebe;
    }

    public BigDecimal getTotalHaber() {
        return totalHaber;
    }

    public void setTotalHaber(BigDecimal totalHaber) {
        this.totalHaber = totalHaber;
    }

    public String getTipoPartida() {
        return tipoPartida;
    }

    public void setTipoPartida(String tipoPartida) {
        this.tipoPartida = tipoPartida;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaContabilizacion() {
        return fechaContabilizacion;
    }

    public void setFechaContabilizacion(Date fechaContabilizacion) {
        this.fechaContabilizacion = fechaContabilizacion;
    }

    @JsonbTransient
    public PeriodoContable getIdPeriodoContable() {
        return idPeriodoContable;
    }

    public void setIdPeriodoContable(PeriodoContable idPeriodoContable) {
        this.idPeriodoContable = idPeriodoContable;
    }

    public List<DetallePartidaDiario> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePartidaDiario> detalles) {
        this.detalles = detalles;
    }
    
    /**
     * Validates that the journal entry is balanced (debe = haber)
     * @return true if balanced, false otherwise
     */
    public boolean estaBalanceada() {
        if (totalDebe == null || totalHaber == null) {
            return false;
        }
        return totalDebe.compareTo(totalHaber) == 0;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPartidaDiario != null ? idPartidaDiario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PartidaDiario)) {
            return false;
        }
        PartidaDiario other = (PartidaDiario) object;
        return !((this.idPartidaDiario == null && other.idPartidaDiario != null) 
                || (this.idPartidaDiario != null && !this.idPartidaDiario.equals(other.idPartidaDiario)));
    }

    @Override
    public String toString() {
        return "PartidaDiario[ idPartidaDiario=" + idPartidaDiario + " ]";
    }
}
