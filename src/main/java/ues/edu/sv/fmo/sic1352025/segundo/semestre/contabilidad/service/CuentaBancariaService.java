package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.service;



import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CuentaBancariaBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CuentaContableBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.CuentaBancariaDTO;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaBancaria;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums.OperacionesBancarias;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums.TablaOrigen;

@Stateless
public class CuentaBancariaService extends AbstractService{


    @Inject
    CuentaBancariaBean cuentaBancariaBean;

    @Inject
    AsientoService asientoService;

    @Inject
    CuentaContableBean cuentaContableBean;

    @Inject
    CabeceraTransaccionService cabeceraTransaccionService;

    public UUID crearCuentaBancaria(CuentaBancariaDTO cuentaBancariaDTO){

        String operacionBancaria = OperacionesBancarias.APERTURA_CUENTA.name();
        /* CREAMOS LA CUENTA BANCARIA */
        UUID idCuentaBancaria = UUID.randomUUID();
        System.err.println(cuentaBancariaDTO);
        CuentaBancaria cuentaBancaria = cuentaBancariaDTO.getCuentaBancaria();
        UUID idSocio = cuentaBancaria.getIdSocio().getIdSocio();
       
        cuentaBancaria.setIdCuentaBancaria(idCuentaBancaria);
        
        cuentaBancariaBean.persistEntity(cuentaBancaria);

        BigDecimal monto = cuentaBancaria.getSaldo();
        /*CUENTAS ASOCIADAS A LA OPERACION */
        List<CuentaContable> listCuentasContablesAsociadas = cuentaContableBean.findByNameOperacionBancaria(operacionBancaria);

        /* GENERAMOS ASIENTOS Y DETALLE ASIENTO */
        asientoService.generarAsientoContable(listCuentasContablesAsociadas, monto, operacionBancaria);

        /* GENERAMOS LA CABECERA DE LA TRANSACCION */ /* TABLA ORIGEN -> cuenta_bancaria */
        cabeceraTransaccionService.crearCabeceraTransaccion(monto, idCuentaBancaria, TablaOrigen.CUENTA_BANCARIA.getTablaOrigen(), operacionBancaria, idSocio);
        
        return idCuentaBancaria;
    }   

    /* SE PUEDE NORMALIZAR LO DE LOS ASIENTOS Y DEMÁS */

    public UUID aporteBancaria(CuentaBancariaDTO cuentaBancariaDTO){

        String operacionBancaria = OperacionesBancarias.APORTACION_CREDITO.name();

        /* APORTE SUMAMOS EN UPDATE A LA CUENTA BANCARIA DTO VIENE CON MONTO DE TRASACCION
            RECUPERAR CREDITO ORIGINAL
            RESTAR MONTOS
            UPDATE
        */

        /* OBTENEMOS LA ENTITY CON VALOR ORIGINAL DESDE BD */
        CuentaBancaria cuentaBancaria = cuentaBancariaDTO.getCuentaBancaria();
        UUID idCuentaBancaria = cuentaBancaria.getIdCuentaBancaria();
        CuentaBancaria cuentaBancariaBD = cuentaBancariaBean.findById(idCuentaBancaria);

        /* ACTUALIZAMOS MONTOS */
        BigDecimal montoBD = cuentaBancariaBD.getSaldo();
        BigDecimal montoAporte = cuentaBancaria.getSaldo();

        BigDecimal monto = montoBD.subtract(montoAporte);

        /* ACTUALIZAMOS ENTITY A HACER UPDATE */
        cuentaBancaria.setSaldo(monto);

        UUID idSocio = cuentaBancaria.getIdSocio().getIdSocio();
       
        cuentaBancaria.setIdCuentaBancaria(idCuentaBancaria);
        
        cuentaBancariaBean.updateEntity(cuentaBancariaBD);

        // BigDecimal monto = cuentaBancaria.getSaldo();
        
        /*CUENTAS ASOCIADAS A LA OPERACION */
        List<CuentaContable> listCuentasContablesAsociadas = cuentaContableBean.findByNameOperacionBancaria(operacionBancaria);

        /* GENERAMOS ASIENTOS Y DETALLE ASIENTO */
        asientoService.generarAsientoContable(listCuentasContablesAsociadas, monto, operacionBancaria);

        /* GENERAMOS LA CABECERA DE LA TRANSACCION */
        cabeceraTransaccionService.crearCabeceraTransaccion(monto, idCuentaBancaria, operacionBancaria, operacionBancaria, idSocio);
        
        return idCuentaBancaria;
    }   
   

}
