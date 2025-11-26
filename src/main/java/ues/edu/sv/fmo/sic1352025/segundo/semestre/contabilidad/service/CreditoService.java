package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.service;

import java.util.List;
import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CreditoBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CuentaContableBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.OperacionBancariaBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.CreditoDTO;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.Credito;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums.OperacionesBancarias;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums.TablaOrigen;

import java.math.BigDecimal;

@Stateless
public class CreditoService {

    @Inject
    CreditoBean creditoBean;

    @Inject 
    OperacionBancariaBean operacionBancariaBean;

   
    @Inject
    CuentaContableBean cuentaContableBean;


    @Inject
    AsientoService asientoService;

    @Inject
    CabeceraTransaccionService cabeceraTransaccionService;

    // List<TipoCuentaOperacion> listTipoCuentaOperacion;

    /**
     *  DEBERÍA RETURN LOS ID'S PARA FORMAR LAS URI
     * @return
     */
    /* 
    public UUID serviceCrearCredito(CreditoDTO creditoDTO){

        BigDecimal totalHaber = BigDecimal.ZERO;
        BigDecimal totalDebe = BigDecimal.ZERO;
        Date fecha = new Date();

        /*
        * CARGAMOS LOS TIPOS DE OPERACION ASOCIADAS 
        */
        // OperacionBancaria operacionBancaria = operacionBancariaBean.findById(fecha);
        //List<CuentaContable> listCuentasContablesAsociadas = cuentaContableBean.findByNameOperacionBancaria(OperacionesBancarias.APERTURA_CREDITO.toString());
        /* 
        List<CuentaContable> listCuentasContablesAsociadas = cuentaContableBean.findByNameOperacionBancaria("APERTURA_CREDITO");


        UUID idCredito = UUID.randomUUID();
        /**
         * SEPARAMOS LOS DATOS DEL DTO
         * CREDITO DTO LA OPERACION ES CREDITO
         
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

        /* CREDITO 
        credito.setFecha(fecha);
        credito.setIdCredito(idCredito);
        creditoBean.persistEntity(credito);

        /* CREDITO_SOCIO 
        CreditoSocio creditoSocio = new CreditoSocio(UUID.randomUUID());
        Socio socio = socioBean.findById(idSocio);
        creditoSocio.setIdCredito(credito);
        creditoSocio.setIdSocio(socio);
        creditoSocio.setActivo(true);   
        creditoSocioBean.persistEntity(creditoSocio);
        
        /* GENERAR DETALLE ASIENTO CON BUCLE 
        for(CuentaContable cuentaContable: listCuentasContablesAsociadas){
            DetalleAsiento detalleAsiento = new DetalleAsiento(UUID.randomUUID());

            /*  QUERIE DETERMINAR ENTRADA O SALIDA SEGÚN RELACION CUENTA-OPERACION
            // String naturalezaDetalleAsiento = cuentaOperacionBean.findNaturalezaCuentaOperacion(OperacionesBancarias.APERTURA_CREDITO.toString(), cuentaContable);
            String naturalezaDetalleAsiento = cuentaOperacionBean.findNaturalezaCuentaOperacion("APERTURA_CREDITO", cuentaContable);
            String naturalezaCuenta = cuentaContable.getNaturaleza();
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
       
        /*SE HACE UPDATE CON LO VALORES 
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
        
        return idCredito;
    }
    */

    public UUID crearCredito(CreditoDTO creditoDTO){

        String operacionBancaria = OperacionesBancarias.APERTURA_CREDITO.name();
        /* CREAMOS EL CREDITO BANCARIO*/
        UUID idCredito = UUID.randomUUID();
        Credito credito = creditoDTO.getCredito();
        UUID idSocio = creditoDTO.getIdSocio();

        credito.setIdCredito(idCredito);
        creditoBean.persistEntity(credito);
        

        BigDecimal monto = credito.getMonto();

        /*CUENTAS ASOCIADAS A LA OPERACION */
        List<CuentaContable> listCuentasContablesAsociadas = cuentaContableBean.findByNameOperacionBancaria(operacionBancaria);

        /* GENERAMOS ASIENTOS Y DETALLE ASIENTO */
        asientoService.generarAsientoContable(listCuentasContablesAsociadas, monto, operacionBancaria);

        /* GENERAMOS LA CABECERA DE LA TRANSACCION */ /* TABLA ORIGEN -> cuenta_bancaria */
        cabeceraTransaccionService.crearCabeceraTransaccion(monto, idCredito, TablaOrigen.CREDITO.getTablaOrigen(), operacionBancaria, idSocio);
        
        return idCredito;
    }


    public void aporteCredito(CreditoDTO creditoDTO){

        /* CONTEXTUALIZAMOS OPERACION BANCARIA */
        String operacionBancaria = OperacionesBancarias.APORTACION_CREDITO.name();


        /* VIENE EL CREDITO DTO COMPLETO SEPARAMOS */
        UUID idSocio = creditoDTO.getIdSocio();
        Credito credito = creditoDTO.getCredito();
        UUID idCredito = credito.getIdCredito();

        /* OBTENEMOS CREDITO DESDE LA BD */
        Credito creditoBD = creditoBean.findById(idCredito);

        /* OBTENEMOS MONTOS PARA OPERAR */
        BigDecimal montoBD = creditoBD.getMonto();
        BigDecimal montoAporte = credito.getMonto();
        BigDecimal monto = montoBD.subtract(montoAporte);

        /* HACEMOS EL UPDATE DEL CREDITO A ACTUALIZAR */
        credito.setMonto(monto);

        /* HACEMOS UPDATE */
        creditoBean.updateEntity(credito);

        /* CUENTAS ASOCIADAS A LA OPERACION- ASIENTOS*/
        List<CuentaContable> listCuentasContablesAsociadas = cuentaContableBean.findByNameOperacionBancaria(operacionBancaria);
        /*ASIENTOS CONTABLES */
        asientoService.generarAsientoContable(listCuentasContablesAsociadas, monto, operacionBancaria);

        /*CABECERA TRANSACCION */
        cabeceraTransaccionService.crearCabeceraTransaccion(monto, idCredito, TablaOrigen.CREDITO.getTablaOrigen(), operacionBancaria, idSocio);

    }



}
