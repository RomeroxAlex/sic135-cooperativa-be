package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.AsientoContableBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CuentaOperacionBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.DetalleAsientoBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.AsientoContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.DetalleAsiento;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums.MovimientoContable;

@Stateless
public class AsientoService extends AbstractService{

    @Inject
    AsientoContableBean asientoContableBean;

    @Inject
    CuentaOperacionBean cuentaOperacionBean;

    @Inject
    DetalleAsientoBean detalleAsientoBean;

    public void generarAsientoContable(List<CuentaContable> listCuentasContablesAsociadas, BigDecimal monto, String operacionBancaria){

        /*CALCULO TOTAL DEBE Y HABER */
        BigDecimal totalDebe = BigDecimal.ZERO;
        BigDecimal totalHaber = BigDecimal.ZERO;


        /* ASIENTO CONTABLE */
        AsientoContable asientoContable = new AsientoContable(UUID.randomUUID());
        asientoContableBean.persistEntity(asientoContable);
        for(CuentaContable cuentaContable: listCuentasContablesAsociadas){
            DetalleAsiento detalleAsiento;

            /*  QUERIE DETERMINAR ENTRADA O SALIDA SEGÚN RELACION CUENTA-OPERACION*/
            String naturalezaOperacion = cuentaOperacionBean.findNaturalezaCuentaOperacion(operacionBancaria, cuentaContable);

            //String naturalezaCuenta = cuentaContable.getNaturaleza();            
            // boolean esDeudor = esMovimientoDeudor(naturalezaCuenta, naturalezaOperacion);

            boolean esDeudor = naturalezaOperacion.equals(MovimientoContable.DEBE.name());

            if(esDeudor){
                totalDebe = totalDebe.add(monto);
                detalleAsiento = generarDetalle(asientoContable, cuentaContable, monto, null);
            }else{
                totalHaber = totalHaber.add(monto);
                detalleAsiento = generarDetalle(asientoContable, cuentaContable, null, monto);
            }

            detalleAsientoBean.persistEntity(detalleAsiento);
        }

        /* GENERAR ASIENTO CONTABLE */
        asientoContableBean.persistEntity(asientoContable);
    }

}
