package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.BalanceInicial;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CatalogoCuenta;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.BusinessException;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ResourceNotFoundException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Stateless
@LocalBean
public class BalanceInicialBean extends AbstractDataPersist<BalanceInicial> implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;

    @Inject
    CatalogoCuentaBean catalogoCuentaBean;

    public BalanceInicialBean() {
        super(BalanceInicial.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<BalanceInicial> findByPeriodo(String periodo) {
        TypedQuery<BalanceInicial> query = em.createNamedQuery("BalanceInicial.findByPeriodo", BalanceInicial.class);
        query.setParameter("periodo", periodo);
        return query.getResultList();
    }

    @Transactional
    public List<BalanceInicial> crearBalanceInicial(String periodo, List<BalanceInicial> balances) {
        // Validate that balance is balanced
        BigDecimal totalDebe = BigDecimal.ZERO;
        BigDecimal totalHaber = BigDecimal.ZERO;
        
        for (BalanceInicial balance : balances) {
            if (balance.getCuenta() == null || balance.getCuenta().getId() == null) {
                throw new BusinessException("MISSING_CUENTA", "La cuenta es requerida para cada balance inicial");
            }
            
            CatalogoCuenta cuenta = catalogoCuentaBean.findById(balance.getCuenta().getId());
            if (cuenta == null) {
                throw new ResourceNotFoundException("CatalogoCuenta", balance.getCuenta().getId());
            }
            balance.setCuenta(cuenta);
            
            BigDecimal debe = balance.getSaldoDebe() != null ? balance.getSaldoDebe() : BigDecimal.ZERO;
            BigDecimal haber = balance.getSaldoHaber() != null ? balance.getSaldoHaber() : BigDecimal.ZERO;
            
            totalDebe = totalDebe.add(debe);
            totalHaber = totalHaber.add(haber);
        }
        
        if (totalDebe.compareTo(totalHaber) != 0) {
            throw new BusinessException("UNBALANCED", 
                "El balance inicial no está balanceado. Debe = " + totalDebe + ", Haber = " + totalHaber);
        }
        
        // Delete existing balance for this period
        TypedQuery<BalanceInicial> existingQuery = em.createNamedQuery("BalanceInicial.findByPeriodo", BalanceInicial.class);
        existingQuery.setParameter("periodo", periodo);
        for (BalanceInicial existing : existingQuery.getResultList()) {
            em.remove(existing);
        }
        
        // Save new balances
        for (BalanceInicial balance : balances) {
            balance.setId(UUID.randomUUID());
            balance.setPeriodo(periodo);
            persistEntity(balance);
        }
        
        return balances;
    }
}
