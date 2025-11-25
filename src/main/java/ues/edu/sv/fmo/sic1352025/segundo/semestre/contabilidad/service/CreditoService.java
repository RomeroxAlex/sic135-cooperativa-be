package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.service;

import java.util.List;
import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.AsientoContableBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CabeceraTransaccionBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CreditoBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CreditoSocioBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CuentaContableBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CuentaOperacionBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.OperacionBancariaBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.SocioBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.TransaccionBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.CreditoDTO;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.AsientoContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.Credito;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CreditoSocio;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.DetalleAsiento;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.Socio;
// import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums.NaturalezaContable;
// import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums.NaturalezaOperacion;
// import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums.OperacionesBancarias;
// import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums.TablaOrigen;

import java.math.BigDecimal;
import java.util.Date;

@Stateless
public class CreditoService {

    @Inject
    CreditoBean creditoBean;

    @Inject
    AsientoContableBean asientoContableBean;

    @Inject
    CreditoSocioBean creditoSocioBean;

    @Inject
    CabeceraTransaccionBean cabeceraTransaccionBean;

    @Inject
    TransaccionBean transaccionBean;

    @Inject
    SocioBean socioBean;

    @Inject 
    OperacionBancariaBean operacionBancariaBean;

    @Inject
    DetalleAsientoBean detalleAsientoBean;

    @Inject
    CuentaContableBean cuentaContableBean;

    @Inject
    CuentaOperacionBean cuentaOperacionBean;

    // List<TipoCuentaOperacion> listTipoCuentaOperacion;

    /**
     *  DEBERÍA RETURN LOS ID'S PARA FORMAR LAS URI NO?
     * @return
     */
    public UUID serviceCrearCredito(CreditoDTO creditoDTO){

        BigDecimal totalHaber = BigDecimal.ZERO;
        BigDecimal totalDebe = BigDecimal.ZERO;
        Date fecha = new Date();

        /*
        * CARGAMOS LOS TIPOS DE OPERACION ASOCIADAS 
        */
        // OperacionBancaria operacionBancaria = operacionBancariaBean.findById(fecha);
        //List<CuentaContable> listCuentasContablesAsociadas = cuentaContableBean.findByNameOperacionBancaria(OperacionesBancarias.APERTURA_CREDITO.toString());
        List<CuentaContable> listCuentasContablesAsociadas = cuentaContableBean.findByNameOperacionBancaria("APERTURA_CREDITO");


        UUID idCredito = UUID.randomUUID();
        /**
         * SEPARAMOS LOS DATOS DEL DTO
         * CREDITO DTO LA OPERACION ES CREDITO
         */
        Credito credito = creditoDTO.getCredito();
        UUID idSocio = creditoDTO.getIdSocio();
        
        AsientoContable asientoContable = new AsientoContable(UUID.randomUUID());
        asientoContable.setTotalDebe(totalDebe);
        asientoContable.setTotalHaber(totalHaber);
        asientoContable.setFecha(fecha);
        asientoContableBean.persistEntity(asientoContable);

        BigDecimal monto = credito.getMonto();
        /**
         * PERSISTENCIA DE:
         * CREDITO
         * CREDITO_SOCIO
         * CABECERA_TRANSACCION
         * TRANSACCION
         */

        /* CREDITO */
        credito.setIdCredito(idCredito);
        creditoBean.persistEntity(credito);

        /* CREDITO_SOCIO */
        CreditoSocio creditoSocio = new CreditoSocio(UUID.randomUUID());
        Socio socio = socioBean.findById(idSocio);
        creditoSocio.setIdCredito(credito);
        creditoSocio.setIdSocio(socio);
        creditoSocio.setActivo(true);   
        creditoSocioBean.persistEntity(creditoSocio);
        
        System.out.println("Antes del bucle");
        /* GENERAR DETALLE ASIENTO CON BUCLE */
        for(CuentaContable cuentaContable: listCuentasContablesAsociadas){
            DetalleAsiento detalleAsiento = new DetalleAsiento(UUID.randomUUID());

            /*  QUERIE DETERMINAR ENTRADA O SALIDA SEGÚN RELACION CUENTA-OPERACION*/
            // String naturalezaDetalleAsiento = cuentaOperacionBean.findNaturalezaCuentaOperacion(OperacionesBancarias.APERTURA_CREDITO.toString(), cuentaContable);
            String naturalezaDetalleAsiento = cuentaOperacionBean.findNaturalezaCuentaOperacion("APERTURA_CREDITO", cuentaContable);
            String naturalezaCuenta = cuentaContable.getNaturaleza();
            System.out.println("Valor Naturaleza: "+  naturalezaCuenta);
            // Depende de la operacion realmente es condicional

            if(naturalezaCuenta.equals("DEUDORA")){
                // DEUDORA + ENTRADA = DEBE
                if (naturalezaDetalleAsiento.equals("ENTRADA")) {
                    totalDebe = totalDebe.add(monto);
                    detalleAsiento.setDebe(monto);
                    detalleAsiento.setIdAsientoContable(asientoContable);
                    detalleAsiento.setIdCuentaContable(cuentaContable);
                }else{ // DEUDORA + SALIDA = HABER

                    totalHaber = totalHaber.add(monto);
                    detalleAsiento.setHaber(monto);
                    detalleAsiento.setIdAsientoContable(asientoContable);
                    detalleAsiento.setIdCuentaContable(cuentaContable);
                }

            }else{
                // ACREEDORA + ENTRADA = HABER
                if(naturalezaDetalleAsiento.equals("ENTRADA")){
                    totalHaber = totalHaber.add(monto);
                    detalleAsiento.setHaber(monto);
                    detalleAsiento.setIdAsientoContable(asientoContable);
                    detalleAsiento.setIdCuentaContable(cuentaContable);
                }else{ // ACREEDORA + SALIDA = DEBE
                    totalDebe = totalDebe.add(monto);
                    detalleAsiento.setDebe(monto);
                    detalleAsiento.setIdAsientoContable(asientoContable);
                    detalleAsiento.setIdCuentaContable(cuentaContable);
                }

            }   
            detalleAsientoBean.persistEntity(detalleAsiento);
        }

        /* ASIENTO CONTABLE */
       
        /*SE HACE UPDATE CON LO VALORES */
        asientoContable.setTotalDebe(totalDebe);
        asientoContable.setTotalHaber(totalHaber);
        asientoContableBean.updateEntity(asientoContable);

        /*CABECERA_TRANSACCION  PRIMERA DE LA LISTA TABLA; PAGO*/
        /* 
        CabeceraTransaccion cabeceraTransaccion = new CabeceraTransaccion(UUID.randomUUID());
        cabeceraTransaccion.setIdSocio(idSocio);
        cabeceraTransaccion.setTablaOrigen(TablaOrigen.CREDITO.getTablaOrigen());
        cabeceraTransaccion.setIdOrigen(idCredito);
        cabeceraTransaccion.setFecha(fecha);
        cabeceraTransaccionBean.persistEntity(cabeceraTransaccion);
        */
        return idCredito;
    }

}
