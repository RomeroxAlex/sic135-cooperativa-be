package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "asiento_contable")
public class AsientoContable implements Serializable{

    @Id
    @Column(name = "id_asiento_contable")
    private UUID idAsientoContable;

    @Column(name = "total_debe")
    private BigDecimal totalDebe;

    @Column(name = "total_haber")
    private BigDecimal totalHaber;

    @Column(name = "fecha")
    private Date fecha;

    @Column(name = "descripcion")
    private String descripcion;

    

    public AsientoContable(UUID idAsientoContable) {
        this.idAsientoContable = idAsientoContable;
    }
    
    public AsientoContable() {}


    public UUID getIdAsientoContable() {
        return idAsientoContable;
    }

    public void setIdAsientoContable(UUID idAsientoContable) {
        this.idAsientoContable = idAsientoContable;
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

    
}
