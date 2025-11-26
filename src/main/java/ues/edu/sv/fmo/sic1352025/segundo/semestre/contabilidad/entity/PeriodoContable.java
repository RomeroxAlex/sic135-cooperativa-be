package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * Entity representing an accounting period for period control.
 * Controls which accounting period is active for journal entries.
 */
@Entity
@Table(name = "periodo_contable")
@NamedQueries({
    @NamedQuery(name = "PeriodoContable.findAll", query = "SELECT p FROM PeriodoContable p ORDER BY p.fechaInicio DESC"),
    @NamedQuery(name = "PeriodoContable.findByActivo", query = "SELECT p FROM PeriodoContable p WHERE p.activo = :activo"),
    @NamedQuery(name = "PeriodoContable.findPeriodoActivo", query = "SELECT p FROM PeriodoContable p WHERE p.activo = true AND p.cerrado = false"),
    @NamedQuery(name = "PeriodoContable.findByFecha", query = "SELECT p FROM PeriodoContable p WHERE :fecha BETWEEN p.fechaInicio AND p.fechaFin")
})
public class PeriodoContable implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "id_periodo_contable")
    private UUID idPeriodoContable;
    
    @Column(name = "nombre")
    private String nombre;
    
    @Column(name = "descripcion")
    private String descripcion;
    
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    
    @Column(name = "activo")
    private Boolean activo;
    
    @Column(name = "cerrado")
    private Boolean cerrado;
    
    @Column(name = "fecha_cierre")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCierre;
    
    @Column(name = "anio")
    private Integer anio;
    
    @Column(name = "mes")
    private Integer mes;

    public PeriodoContable() {
    }

    public PeriodoContable(UUID idPeriodoContable) {
        this.idPeriodoContable = idPeriodoContable;
    }

    public UUID getIdPeriodoContable() {
        return idPeriodoContable;
    }

    public void setIdPeriodoContable(UUID idPeriodoContable) {
        this.idPeriodoContable = idPeriodoContable;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean getCerrado() {
        return cerrado;
    }

    public void setCerrado(Boolean cerrado) {
        this.cerrado = cerrado;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPeriodoContable != null ? idPeriodoContable.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof PeriodoContable)) {
            return false;
        }
        PeriodoContable other = (PeriodoContable) object;
        return !((this.idPeriodoContable == null && other.idPeriodoContable != null) 
                || (this.idPeriodoContable != null && !this.idPeriodoContable.equals(other.idPeriodoContable)));
    }

    @Override
    public String toString() {
        return "PeriodoContable[ idPeriodoContable=" + idPeriodoContable + " ]";
    }
}
