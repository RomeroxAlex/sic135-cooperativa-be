package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "detalle_asiento")
public class DetalleAsiento implements Serializable{

    @Id
    @Column(name = "id_detalle_asiento")
    private UUID idDetalleAsiento;

    @JoinColumn(name = "id_asiento_contable")
    @ManyToOne
    private AsientoContable idAsientoContable;

    @JoinColumn(name = "id_cuenta_contable")
    @ManyToOne
    private CuentaContable idCuentaContable;

    @Column(name = "debe")
    private BigDecimal debe;

    @Column(name = "haber") 
    private BigDecimal haber;

    public DetalleAsiento() {}


    public DetalleAsiento(UUID idDetalleAsiento) {
        this.idDetalleAsiento = idDetalleAsiento;
    }

    public UUID getIdDetalleAsiento() {
        return idDetalleAsiento;
    }

    public void setIdDetalleAsiento(UUID idDetalleAsiento) {
        this.idDetalleAsiento = idDetalleAsiento;
    }

    public AsientoContable getIdAsientoContable() {
        return idAsientoContable;
    }

    public void setIdAsientoContable(AsientoContable idAsientoContable) {
        this.idAsientoContable = idAsientoContable;
    }

    public CuentaContable getIdCuentaContable() {
        return idCuentaContable;
    }

    public void setIdCuentaContable(CuentaContable idCuentaContable) {
        this.idCuentaContable = idCuentaContable;
    }

    public BigDecimal getDebe() {
        return debe;
    }

    public void setDebe(BigDecimal debe) {
        this.debe = debe;
    }

    public BigDecimal getHaber() {
        return haber;
    }

    public void setHaber(BigDecimal haber) {
        this.haber = haber;
    }


    
}
