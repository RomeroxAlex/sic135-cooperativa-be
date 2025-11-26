package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CabeceraTransaccionBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.OperacionBancariaBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.TransaccionBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CabeceraTransaccion;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.OperacionBancaria;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.Transaccion;

@Stateless
public class CabeceraTransaccionService extends AbstractService{

    @Inject
    OperacionBancariaBean operacionBancariaBean;

    @Inject
    CabeceraTransaccionBean cabeceraTransaccionBean;

    @Inject
    TransaccionBean transaccionBean;

    public void crearCabeceraTransaccion(BigDecimal monto, UUID idOrigen, String tablaOrigen, String nombreOperacionBancaria, UUID idSocio){

        /* REMOVE */
        Transaccion transaccion = new Transaccion(UUID.randomUUID());
        transaccionBean.persistEntity(transaccion);

        /* QUERY PARA OBTENER LA ENTITY OPERACION POR NOMBRE */
        OperacionBancaria operacionBancaria = operacionBancariaBean.findByName(nombreOperacionBancaria);

        /* ARMAMOS CABECERA */
        CabeceraTransaccion cabeceraTransaccion = new CabeceraTransaccion(UUID.randomUUID());
        cabeceraTransaccion.setIdOperacionBancaria(operacionBancaria);
        cabeceraTransaccion.setFecha(new Date());
        cabeceraTransaccion.setIdOrigen(idOrigen);
        cabeceraTransaccion.setTablaOrigen(tablaOrigen);
        cabeceraTransaccion.setMonto(monto);
        cabeceraTransaccion.setIdSocio(idSocio);
        cabeceraTransaccion.setIdTransaccion(transaccion);
        cabeceraTransaccionBean.persistEntity(cabeceraTransaccion);
    }


}
