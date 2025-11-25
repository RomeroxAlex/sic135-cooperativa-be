package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.MayorResponseDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.MovimientoDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CatalogoCuenta;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.LineaPartida;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.MovimientoMayor;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PartidaDiario;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ResourceNotFoundException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Stateless
@LocalBean
public class MayorBean extends AbstractDataPersist<MovimientoMayor> implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;

    @Inject
    CatalogoCuentaBean catalogoCuentaBean;

    public MayorBean() {
        super(MovimientoMayor.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public MayorResponseDto getMayorByCuenta(UUID cuentaId, LocalDate fechaInicio, LocalDate fechaFin) {
        CatalogoCuenta cuenta = catalogoCuentaBean.findById(cuentaId);
        if (cuenta == null) {
            throw new ResourceNotFoundException("CatalogoCuenta", cuentaId);
        }

        MayorResponseDto response = new MayorResponseDto();
        response.setCuentaId(cuentaId);
        response.setCuentaCodigo(cuenta.getCodigo());
        response.setCuentaNombre(cuenta.getNombre());
        response.setFechaInicio(fechaInicio);
        response.setFechaFin(fechaFin);

        // Get movements from partidas posteadas
        TypedQuery<Object[]> query = em.createQuery(
            "SELECT lp, pd FROM LineaPartida lp " +
            "JOIN lp.partidaDiario pd " +
            "WHERE lp.cuenta.id = :cuentaId " +
            "AND pd.estado = 'POSTEADA' " +
            "AND pd.fecha BETWEEN :fechaInicio AND :fechaFin " +
            "ORDER BY pd.fecha, pd.numeroPartida", Object[].class);
        query.setParameter("cuentaId", cuentaId);
        query.setParameter("fechaInicio", fechaInicio);
        query.setParameter("fechaFin", fechaFin);

        List<Object[]> results = query.getResultList();
        
        BigDecimal saldoInicial = getSaldoInicial(cuentaId, fechaInicio);
        response.setSaldoInicial(saldoInicial);
        
        BigDecimal saldoAcumulado = saldoInicial;
        List<MovimientoDto> movimientos = new ArrayList<>();
        
        for (Object[] row : results) {
            LineaPartida linea = (LineaPartida) row[0];
            PartidaDiario partida = (PartidaDiario) row[1];
            
            MovimientoDto mov = new MovimientoDto();
            mov.setId(linea.getId());
            mov.setFecha(partida.getFecha());
            mov.setDescripcion(linea.getDescripcion() != null ? linea.getDescripcion() : partida.getDescripcion());
            mov.setReferencia(partida.getReferencia());
            mov.setNumeroPartida(partida.getNumeroPartida());
            mov.setDebe(linea.getDebe());
            mov.setHaber(linea.getHaber());
            
            // Calculate running balance
            BigDecimal debe = linea.getDebe() != null ? linea.getDebe() : BigDecimal.ZERO;
            BigDecimal haber = linea.getHaber() != null ? linea.getHaber() : BigDecimal.ZERO;
            saldoAcumulado = saldoAcumulado.add(debe).subtract(haber);
            mov.setSaldo(saldoAcumulado);
            
            movimientos.add(mov);
        }
        
        response.setMovimientos(movimientos);
        response.setSaldoFinal(saldoAcumulado);
        
        return response;
    }

    private BigDecimal getSaldoInicial(UUID cuentaId, LocalDate fecha) {
        TypedQuery<Object[]> query = em.createQuery(
            "SELECT COALESCE(SUM(lp.debe), 0), COALESCE(SUM(lp.haber), 0) FROM LineaPartida lp " +
            "JOIN lp.partidaDiario pd " +
            "WHERE lp.cuenta.id = :cuentaId " +
            "AND pd.estado = 'POSTEADA' " +
            "AND pd.fecha < :fecha", Object[].class);
        query.setParameter("cuentaId", cuentaId);
        query.setParameter("fecha", fecha);
        
        Object[] result = query.getSingleResult();
        BigDecimal totalDebe = (BigDecimal) result[0];
        BigDecimal totalHaber = (BigDecimal) result[1];
        
        return totalDebe.subtract(totalHaber);
    }

    @Transactional
    public void generarMayorizacion(String periodo) {
        // Parse period (format: YYYY-MM or YYYY)
        LocalDate fechaInicio;
        LocalDate fechaFin;
        
        if (periodo.length() == 7) { // YYYY-MM
            fechaInicio = LocalDate.parse(periodo + "-01");
            fechaFin = fechaInicio.plusMonths(1).minusDays(1);
        } else { // YYYY
            fechaInicio = LocalDate.parse(periodo + "-01-01");
            fechaFin = LocalDate.parse(periodo + "-12-31");
        }
        
        // Get all accounts with movements
        TypedQuery<UUID> accountQuery = em.createQuery(
            "SELECT DISTINCT lp.cuenta.id FROM LineaPartida lp " +
            "JOIN lp.partidaDiario pd " +
            "WHERE pd.estado = 'POSTEADA' " +
            "AND pd.fecha BETWEEN :fechaInicio AND :fechaFin", UUID.class);
        accountQuery.setParameter("fechaInicio", fechaInicio);
        accountQuery.setParameter("fechaFin", fechaFin);
        
        List<UUID> cuentaIds = accountQuery.getResultList();
        
        for (UUID cuentaId : cuentaIds) {
            CatalogoCuenta cuenta = catalogoCuentaBean.findById(cuentaId);
            BigDecimal saldo = getSaldoInicial(cuentaId, fechaInicio);
            
            TypedQuery<Object[]> movQuery = em.createQuery(
                "SELECT lp, pd FROM LineaPartida lp " +
                "JOIN lp.partidaDiario pd " +
                "WHERE lp.cuenta.id = :cuentaId " +
                "AND pd.estado = 'POSTEADA' " +
                "AND pd.fecha BETWEEN :fechaInicio AND :fechaFin " +
                "ORDER BY pd.fecha, pd.numeroPartida", Object[].class);
            movQuery.setParameter("cuentaId", cuentaId);
            movQuery.setParameter("fechaInicio", fechaInicio);
            movQuery.setParameter("fechaFin", fechaFin);
            
            for (Object[] row : movQuery.getResultList()) {
                LineaPartida linea = (LineaPartida) row[0];
                PartidaDiario partida = (PartidaDiario) row[1];
                
                BigDecimal debe = linea.getDebe() != null ? linea.getDebe() : BigDecimal.ZERO;
                BigDecimal haber = linea.getHaber() != null ? linea.getHaber() : BigDecimal.ZERO;
                saldo = saldo.add(debe).subtract(haber);
                
                MovimientoMayor movimiento = new MovimientoMayor();
                movimiento.setId(UUID.randomUUID());
                movimiento.setCuenta(cuenta);
                movimiento.setPartidaDiario(partida);
                movimiento.setFecha(partida.getFecha());
                movimiento.setDescripcion(linea.getDescripcion());
                movimiento.setReferencia(partida.getReferencia());
                movimiento.setDebe(debe);
                movimiento.setHaber(haber);
                movimiento.setSaldo(saldo);
                
                persistEntity(movimiento);
            }
        }
    }
}
