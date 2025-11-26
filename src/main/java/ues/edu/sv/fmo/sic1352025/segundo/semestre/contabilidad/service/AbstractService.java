package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.service;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.Date;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.AsientoContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.DetalleAsiento;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums.NaturalezaContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums.NaturalezaOperacion;

public abstract class AbstractService {

    public boolean esMovimientoDeudor(String naturalezaCuenta, String naturalezaOperacion){
        boolean esCuentaDeudora = NaturalezaContable.DEUDORA.toString().equals(naturalezaCuenta) ;
        boolean esEntrada = NaturalezaOperacion.ENTRADA.toString().equals(naturalezaOperacion);

        return (esCuentaDeudora && esEntrada) || (!esCuentaDeudora && !esEntrada);
    }

    public DetalleAsiento generarDetalle(AsientoContable asientoContable, CuentaContable cuentaContable, BigDecimal debe, BigDecimal haber){
        DetalleAsiento detalleAsiento = new DetalleAsiento(UUID.randomUUID());
        detalleAsiento.setIdAsientoContable(asientoContable);
        detalleAsiento.setDebe(debe);
        detalleAsiento.setHaber(haber);
        detalleAsiento.setFecha(new Date());
        detalleAsiento.setIdCuentaContable(cuentaContable);
        return detalleAsiento;
    }

}
