package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * Entity representing an adjustment entry (Partida de Ajuste).
 * Adjustment entries are used to correct balances at period end.
 * Types: DEPRECIACION, AMORTIZACION, PROVISION, REGULARIZACION, RECLASIFICACION, CORRECCION
 */
@Entity
@Table(name = "partida_ajuste")
@NamedQueries({
    @NamedQuery(name = "PartidaAjuste.findAll", query = "SELECT p FROM PartidaAjuste p ORDER BY p.fecha DESC"),
    @NamedQuery(name = "PartidaAjuste.findByPeriodo", query = "SELECT p FROM PartidaAjuste p WHERE p.idPeriodoContable = :periodo"),
    @NamedQuery(name = "PartidaAjuste.findByTipo", query = "SELECT p FROM PartidaAjuste p WHERE p.tipoAjuste = :tipo"),
    @NamedQuery(name = "PartidaAjuste.findByEstado", query = "SELECT p FROM PartidaAjuste p WHERE p.estado = :estado"),
    @NamedQuery(name = "PartidaAjuste.findPendientes", query = "SELECT p FROM PartidaAjuste p WHERE p.estado = 'PENDIENTE'"),
    @NamedQuery(name = "PartidaAjuste.findByAutomatico", query = "SELECT p FROM PartidaAjuste p WHERE p.automatico = :automatico")
})
public class PartidaAjuste implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "id_partida_ajuste")
    private UUID idPartidaAjuste;
    
    @Column(name = "numero_ajuste")
    private Integer numeroAjuste;
    
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "concepto")
    private String concepto;
    
    @Column(name = "tipo_ajuste")
    private String tipoAjuste; // DEPRECIACION, AMORTIZACION, PROVISION, REGULARIZACION, RECLASIFICACION, CORRECCION
    
    @Column(name = "estado")
    private String estado; // PENDIENTE, APLICADO, ANULADO
    
    @Column(name = "automatico")
    private Boolean automatico; // true if system generated, false if manual
    
    @Column(name = "monto")
    private BigDecimal monto;
    
    @Column(name = "referencia")
    private String referencia;
    
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    
    @Column(name = "fecha_aplicacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAplicacion;
    
    @JoinColumn(name = "id_periodo_contable", referencedColumnName = "id_periodo_contable")
    @ManyToOne(fetch = FetchType.LAZY)
    private PeriodoContable idPeriodoContable;
    
    @JoinColumn(name = "id_cuenta_debe", referencedColumnName = "id_cuenta_contable")
    @ManyToOne(fetch = FetchType.LAZY)
    private CuentaContable idCuentaDebe;
    
    @JoinColumn(name = "id_cuenta_haber", referencedColumnName = "id_cuenta_contable")
    @ManyToOne(fetch = FetchType.LAZY)
    private CuentaContable idCuentaHaber;
    
    @JoinColumn(name = "id_partida_diario", referencedColumnName = "id_partida_diario")
    @ManyToOne(fetch = FetchType.LAZY)
    private PartidaDiario idPartidaDiario; // Reference to the generated journal entry

    public PartidaAjuste() {
    }

    public PartidaAjuste(UUID idPartidaAjuste) {
        this.idPartidaAjuste = idPartidaAjuste;
    }

    public UUID getIdPartidaAjuste() {
        return idPartidaAjuste;
    }

    public void setIdPartidaAjuste(UUID idPartidaAjuste) {
        this.idPartidaAjuste = idPartidaAjuste;
    }

    public Integer getNumeroAjuste() {
        return numeroAjuste;
    }

    public void setNumeroAjuste(Integer numeroAjuste) {
        this.numeroAjuste = numeroAjuste;
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

    public String getTipoAjuste() {
        return tipoAjuste;
    }

    public void setTipoAjuste(String tipoAjuste) {
        this.tipoAjuste = tipoAjuste;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Boolean getAutomatico() {
        return automatico;
    }

    public void setAutomatico(Boolean automatico) {
        this.automatico = automatico;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
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

    public Date getFechaAplicacion() {
        return fechaAplicacion;
    }

    public void setFechaAplicacion(Date fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }

    @JsonbTransient
    public PeriodoContable getIdPeriodoContable() {
        return idPeriodoContable;
    }

    public void setIdPeriodoContable(PeriodoContable idPeriodoContable) {
        this.idPeriodoContable = idPeriodoContable;
    }

    public CuentaContable getIdCuentaDebe() {
        return idCuentaDebe;
    }

    public void setIdCuentaDebe(CuentaContable idCuentaDebe) {
        this.idCuentaDebe = idCuentaDebe;
    }

    public CuentaContable getIdCuentaHaber() {
        return idCuentaHaber;
    }

    public void setIdCuentaHaber(CuentaContable idCuentaHaber) {
        this.idCuentaHaber = idCuentaHaber;
    }

    @JsonbTransient
    public PartidaDiario getIdPartidaDiario() {
        return idPartidaDiario;
    }

    public void setIdPartidaDiario(PartidaDiario idPartidaDiario) {
        this.idPartidaDiario = idPartidaDiario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPartidaAjuste != null ? idPartidaAjuste.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PartidaAjuste)) {
            return false;
        }
        PartidaAjuste other = (PartidaAjuste) object;
        return !((this.idPartidaAjuste == null && other.idPartidaAjuste != null) 
                || (this.idPartidaAjuste != null && !this.idPartidaAjuste.equals(other.idPartidaAjuste)));
    }

    @Override
    public String toString() {
        return "PartidaAjuste[ idPartidaAjuste=" + idPartidaAjuste + " ]";
    }
}
