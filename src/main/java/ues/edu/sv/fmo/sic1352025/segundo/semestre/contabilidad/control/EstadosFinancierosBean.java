package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.BalanceGeneralDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.EstadoResultadoDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.RubroBalanceDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CatalogoCuenta;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Stateless
@LocalBean
public class EstadosFinancierosBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;

    @Inject
    CatalogoCuentaBean catalogoCuentaBean;

    public BalanceGeneralDto generarBalanceGeneral(String periodo) {
        LocalDate fechaFin = parsePeriodoToFechaFin(periodo);
        
        BalanceGeneralDto balance = new BalanceGeneralDto();
        balance.setPeriodo(periodo);
        
        // Get accounts by type with their balances
        List<RubroBalanceDto> activos = getBalancesByTipo("ACTIVO", fechaFin);
        List<RubroBalanceDto> pasivos = getBalancesByTipo("PASIVO", fechaFin);
        List<RubroBalanceDto> patrimonio = getBalancesByTipo("PATRIMONIO", fechaFin);
        
        // For simplicity, all activos go to corrientes (in real implementation, 
        // you would classify based on account codes or metadata)
        balance.setActivosCorrientes(activos);
        balance.setPasivosCorrientes(pasivos);
        balance.setPatrimonio(patrimonio);
        
        // Calculate totals
        BigDecimal totalActivos = activos.stream()
            .map(RubroBalanceDto::getSaldo)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPasivos = pasivos.stream()
            .map(RubroBalanceDto::getSaldo)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPatrimonio = patrimonio.stream()
            .map(RubroBalanceDto::getSaldo)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        balance.setTotalActivos(totalActivos);
        balance.setTotalPasivos(totalPasivos);
        balance.setTotalPatrimonio(totalPatrimonio);
        balance.setTotalPasivosPatrimonio(totalPasivos.add(totalPatrimonio));
        
        return balance;
    }

    public EstadoResultadoDto generarEstadoResultado(String periodo) {
        LocalDate fechaInicio = parsePeriodoToFechaInicio(periodo);
        LocalDate fechaFin = parsePeriodoToFechaFin(periodo);
        
        EstadoResultadoDto estado = new EstadoResultadoDto();
        estado.setPeriodo(periodo);
        
        // Get accounts by type with their balances for the period
        List<RubroBalanceDto> ingresos = getBalancesByTipoYPeriodo("INGRESO", fechaInicio, fechaFin);
        List<RubroBalanceDto> costos = getBalancesByTipoYPeriodo("COSTO", fechaInicio, fechaFin);
        List<RubroBalanceDto> gastos = getBalancesByTipoYPeriodo("GASTO", fechaInicio, fechaFin);
        
        estado.setIngresos(ingresos);
        estado.setCostos(costos);
        estado.setGastos(gastos);
        
        // Calculate totals
        BigDecimal totalIngresos = ingresos.stream()
            .map(RubroBalanceDto::getSaldo)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCostos = costos.stream()
            .map(RubroBalanceDto::getSaldo)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalGastos = gastos.stream()
            .map(RubroBalanceDto::getSaldo)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        estado.setTotalIngresos(totalIngresos);
        estado.setTotalCostos(totalCostos);
        estado.setTotalGastos(totalGastos);
        estado.setUtilidadBruta(totalIngresos.subtract(totalCostos));
        estado.setUtilidadNeta(totalIngresos.subtract(totalCostos).subtract(totalGastos));
        
        return estado;
    }

    private List<RubroBalanceDto> getBalancesByTipo(String tipo, LocalDate fechaFin) {
        TypedQuery<Object[]> query = em.createQuery(
            "SELECT c.id, c.codigo, c.nombre, " +
            "COALESCE(SUM(lp.debe), 0), COALESCE(SUM(lp.haber), 0) " +
            "FROM CatalogoCuenta c " +
            "LEFT JOIN LineaPartida lp ON lp.cuenta = c " +
            "LEFT JOIN lp.partidaDiario pd ON pd.estado = 'POSTEADA' AND pd.fecha <= :fechaFin " +
            "WHERE c.tipo = :tipo AND c.activo = true " +
            "GROUP BY c.id, c.codigo, c.nombre " +
            "ORDER BY c.codigo", Object[].class);
        query.setParameter("tipo", tipo);
        query.setParameter("fechaFin", fechaFin);
        
        return query.getResultList().stream()
            .map(row -> {
                BigDecimal debe = (BigDecimal) row[3];
                BigDecimal haber = (BigDecimal) row[4];
                BigDecimal saldo = "ACTIVO".equals(tipo) || "GASTO".equals(tipo) || "COSTO".equals(tipo) 
                    ? debe.subtract(haber) 
                    : haber.subtract(debe);
                return new RubroBalanceDto((UUID) row[0], (String) row[1], (String) row[2], saldo);
            })
            .filter(dto -> dto.getSaldo().compareTo(BigDecimal.ZERO) != 0)
            .toList();
    }

    private List<RubroBalanceDto> getBalancesByTipoYPeriodo(String tipo, LocalDate fechaInicio, LocalDate fechaFin) {
        TypedQuery<Object[]> query = em.createQuery(
            "SELECT c.id, c.codigo, c.nombre, " +
            "COALESCE(SUM(lp.debe), 0), COALESCE(SUM(lp.haber), 0) " +
            "FROM CatalogoCuenta c " +
            "LEFT JOIN LineaPartida lp ON lp.cuenta = c " +
            "LEFT JOIN lp.partidaDiario pd ON pd.estado = 'POSTEADA' " +
            "AND pd.fecha BETWEEN :fechaInicio AND :fechaFin " +
            "WHERE c.tipo = :tipo AND c.activo = true " +
            "GROUP BY c.id, c.codigo, c.nombre " +
            "ORDER BY c.codigo", Object[].class);
        query.setParameter("tipo", tipo);
        query.setParameter("fechaInicio", fechaInicio);
        query.setParameter("fechaFin", fechaFin);
        
        return query.getResultList().stream()
            .map(row -> {
                BigDecimal debe = (BigDecimal) row[3];
                BigDecimal haber = (BigDecimal) row[4];
                BigDecimal saldo = "INGRESO".equals(tipo) ? haber.subtract(debe) : debe.subtract(haber);
                return new RubroBalanceDto((UUID) row[0], (String) row[1], (String) row[2], saldo);
            })
            .filter(dto -> dto.getSaldo().compareTo(BigDecimal.ZERO) != 0)
            .toList();
    }

    private LocalDate parsePeriodoToFechaInicio(String periodo) {
        if (periodo.length() == 7) { // YYYY-MM
            return LocalDate.parse(periodo + "-01");
        } else { // YYYY
            return LocalDate.parse(periodo + "-01-01");
        }
    }

    private LocalDate parsePeriodoToFechaFin(String periodo) {
        if (periodo.length() == 7) { // YYYY-MM
            LocalDate inicio = LocalDate.parse(periodo + "-01");
            return inicio.plusMonths(1).minusDays(1);
        } else { // YYYY
            return LocalDate.parse(periodo + "-12-31");
        }
    }
}
