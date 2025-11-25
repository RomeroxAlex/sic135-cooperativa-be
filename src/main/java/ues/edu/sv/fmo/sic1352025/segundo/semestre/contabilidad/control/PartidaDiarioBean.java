package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CatalogoCuenta;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.LineaPartida;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PartidaDiario;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.BusinessException;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ResourceNotFoundException;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Stateless
@LocalBean
public class PartidaDiarioBean extends AbstractDataPersist<PartidaDiario> implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;

    public PartidaDiarioBean() {
        super(PartidaDiario.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<PartidaDiario> findAllOrdered() {
        TypedQuery<PartidaDiario> query = em.createNamedQuery("PartidaDiario.findAll", PartidaDiario.class);
        return query.getResultList();
    }

    public List<PartidaDiario> findByFechaRange(LocalDate fechaInicio, LocalDate fechaFin) {
        TypedQuery<PartidaDiario> query = em.createNamedQuery("PartidaDiario.findByFechaRange", PartidaDiario.class);
        query.setParameter("fechaInicio", fechaInicio);
        query.setParameter("fechaFin", fechaFin);
        return query.getResultList();
    }

    public List<PartidaDiario> findByEstado(String estado) {
        TypedQuery<PartidaDiario> query = em.createNamedQuery("PartidaDiario.findByEstado", PartidaDiario.class);
        query.setParameter("estado", estado);
        return query.getResultList();
    }

    public List<PartidaDiario> findPaginated(int page, int size, String estado, LocalDate fechaInicio, LocalDate fechaFin) {
        StringBuilder jpql = new StringBuilder("SELECT p FROM PartidaDiario p WHERE 1=1");
        
        if (estado != null && !estado.isEmpty()) {
            jpql.append(" AND p.estado = :estado");
        }
        if (fechaInicio != null && fechaFin != null) {
            jpql.append(" AND p.fecha BETWEEN :fechaInicio AND :fechaFin");
        }
        jpql.append(" ORDER BY p.fecha DESC");
        
        TypedQuery<PartidaDiario> query = em.createQuery(jpql.toString(), PartidaDiario.class);
        
        if (estado != null && !estado.isEmpty()) {
            query.setParameter("estado", estado);
        }
        if (fechaInicio != null && fechaFin != null) {
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
        }
        
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    public long count() {
        TypedQuery<Long> query = em.createNamedQuery("PartidaDiario.countAll", Long.class);
        return query.getSingleResult();
    }

    public Long getNextNumeroPartida() {
        TypedQuery<Long> query = em.createQuery("SELECT COALESCE(MAX(p.numeroPartida), 0) + 1 FROM PartidaDiario p", Long.class);
        return query.getSingleResult();
    }

    @Transactional
    public PartidaDiario create(PartidaDiario partida) {
        partida.setId(UUID.randomUUID());
        partida.setNumeroPartida(getNextNumeroPartida());
        partida.setEstado("BORRADOR");
        
        for (LineaPartida linea : partida.getLineas()) {
            linea.setId(UUID.randomUUID());
            linea.setPartidaDiario(partida);
        }
        
        partida.recalcularTotales();
        
        if (!partida.estaBalanceada()) {
            throw new BusinessException("UNBALANCED", "La partida no está balanceada. Debe = " + 
                partida.getTotalDebe() + ", Haber = " + partida.getTotalHaber());
        }
        
        persistEntity(partida);
        return partida;
    }

    @Transactional
    public PartidaDiario update(UUID id, PartidaDiario partida) {
        PartidaDiario existing = findById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("PartidaDiario", id);
        }
        
        if ("POSTEADA".equals(existing.getEstado())) {
            throw new BusinessException("LOCKED", "No se puede modificar una partida posteada");
        }
        
        existing.setFecha(partida.getFecha());
        existing.setDescripcion(partida.getDescripcion());
        existing.setReferencia(partida.getReferencia());
        existing.setEsAjuste(partida.getEsAjuste());
        existing.setMotivoAjuste(partida.getMotivoAjuste());
        existing.setUsuarioModificacion(partida.getUsuarioModificacion());
        
        existing.getLineas().clear();
        for (LineaPartida linea : partida.getLineas()) {
            linea.setId(UUID.randomUUID());
            existing.addLinea(linea);
        }
        
        existing.recalcularTotales();
        
        if (!existing.estaBalanceada()) {
            throw new BusinessException("UNBALANCED", "La partida no está balanceada. Debe = " + 
                existing.getTotalDebe() + ", Haber = " + existing.getTotalHaber());
        }
        
        return updateEntity(existing);
    }

    @Transactional
    public PartidaDiario postear(UUID id) {
        PartidaDiario partida = findById(id);
        if (partida == null) {
            throw new ResourceNotFoundException("PartidaDiario", id);
        }
        
        if (!"BORRADOR".equals(partida.getEstado())) {
            throw new BusinessException("INVALID_STATE", "Solo se pueden postear partidas en estado BORRADOR");
        }
        
        if (!partida.estaBalanceada()) {
            throw new BusinessException("UNBALANCED", "No se puede postear una partida no balanceada");
        }
        
        partida.setEstado("POSTEADA");
        return updateEntity(partida);
    }

    @Transactional
    public PartidaDiario anular(UUID id) {
        PartidaDiario partida = findById(id);
        if (partida == null) {
            throw new ResourceNotFoundException("PartidaDiario", id);
        }
        
        partida.setEstado("ANULADA");
        return updateEntity(partida);
    }

    public List<PartidaDiario> findAjustes() {
        TypedQuery<PartidaDiario> query = em.createQuery(
            "SELECT p FROM PartidaDiario p WHERE p.esAjuste = true ORDER BY p.fecha DESC", 
            PartidaDiario.class);
        return query.getResultList();
    }
}
