package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.BalanzaDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CatalogoCuenta;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Stateless
@LocalBean
public class BalanzaBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;

    @Inject
    CatalogoCuentaBean catalogoCuentaBean;

    public List<BalanzaDto> generarBalanzaComprobacion(LocalDate fechaDesde, LocalDate fechaHasta) {
        List<BalanzaDto> balanza = new ArrayList<>();
        
        // Get all active accounts
        List<CatalogoCuenta> cuentas = catalogoCuentaBean.findAllActive();
        
        for (CatalogoCuenta cuenta : cuentas) {
            BalanzaDto dto = new BalanzaDto();
            dto.setCuentaId(cuenta.getId());
            dto.setCodigo(cuenta.getCodigo());
            dto.setNombre(cuenta.getNombre());
            dto.setTipo(cuenta.getTipo());
            dto.setNivel(cuenta.getNivel());
            
            // Get saldo anterior
            BigDecimal saldoAnterior = getSaldoAnterior(cuenta.getId(), fechaDesde);
            dto.setSaldoAnterior(saldoAnterior);
            
            // Get movements in period
            TypedQuery<Object[]> query = em.createQuery(
                "SELECT COALESCE(SUM(lp.debe), 0), COALESCE(SUM(lp.haber), 0) FROM LineaPartida lp " +
                "JOIN lp.partidaDiario pd " +
                "WHERE lp.cuenta.id = :cuentaId " +
                "AND pd.estado = 'POSTEADA' " +
                "AND pd.fecha BETWEEN :fechaDesde AND :fechaHasta", Object[].class);
            query.setParameter("cuentaId", cuenta.getId());
            query.setParameter("fechaDesde", fechaDesde);
            query.setParameter("fechaHasta", fechaHasta);
            
            Object[] result = query.getSingleResult();
            dto.setDebe((BigDecimal) result[0]);
            dto.setHaber((BigDecimal) result[1]);
            
            // Calculate final balance
            BigDecimal saldo = saldoAnterior.add(dto.getDebe()).subtract(dto.getHaber());
            dto.setSaldo(saldo);
            
            // Only include accounts with movements or balance
            if (saldoAnterior.compareTo(BigDecimal.ZERO) != 0 || 
                dto.getDebe().compareTo(BigDecimal.ZERO) != 0 ||
                dto.getHaber().compareTo(BigDecimal.ZERO) != 0) {
                balanza.add(dto);
            }
        }
        
        return balanza;
    }

    private BigDecimal getSaldoAnterior(java.util.UUID cuentaId, LocalDate fecha) {
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

    public String exportToCsv(List<BalanzaDto> balanza) {
        StringBuilder csv = new StringBuilder();
        csv.append("Código,Nombre,Tipo,Saldo Anterior,Debe,Haber,Saldo\n");
        
        for (BalanzaDto b : balanza) {
            csv.append(String.format("%s,%s,%s,%s,%s,%s,%s\n",
                b.getCodigo(),
                "\"" + b.getNombre().replace("\"", "\"\"") + "\"",
                b.getTipo(),
                b.getSaldoAnterior(),
                b.getDebe(),
                b.getHaber(),
                b.getSaldo()));
        }
        
        return csv.toString();
    }
}
