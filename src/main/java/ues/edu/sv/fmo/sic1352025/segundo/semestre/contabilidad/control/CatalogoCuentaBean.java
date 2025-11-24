package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CatalogoCuenta;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.BusinessException;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ResourceNotFoundException;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Stateless
@LocalBean
public class CatalogoCuentaBean extends AbstractDataPersist<CatalogoCuenta> implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;

    public CatalogoCuentaBean() {
        super(CatalogoCuenta.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<CatalogoCuenta> findAllActive() {
        TypedQuery<CatalogoCuenta> query = em.createNamedQuery("CatalogoCuenta.findAll", CatalogoCuenta.class);
        return query.getResultList();
    }

    public List<CatalogoCuenta> findByTipo(String tipo) {
        TypedQuery<CatalogoCuenta> query = em.createNamedQuery("CatalogoCuenta.findByTipo", CatalogoCuenta.class);
        query.setParameter("tipo", tipo);
        return query.getResultList();
    }

    public Optional<CatalogoCuenta> findByCodigo(String codigo) {
        TypedQuery<CatalogoCuenta> query = em.createNamedQuery("CatalogoCuenta.findByCodigo", CatalogoCuenta.class);
        query.setParameter("codigo", codigo);
        List<CatalogoCuenta> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public List<CatalogoCuenta> findPaginated(int page, int size, String tipo) {
        TypedQuery<CatalogoCuenta> query;
        if (tipo != null && !tipo.isEmpty()) {
            query = em.createNamedQuery("CatalogoCuenta.findByTipo", CatalogoCuenta.class);
            query.setParameter("tipo", tipo);
        } else {
            query = em.createNamedQuery("CatalogoCuenta.findAll", CatalogoCuenta.class);
        }
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    public long count() {
        TypedQuery<Long> query = em.createNamedQuery("CatalogoCuenta.countAll", Long.class);
        return query.getSingleResult();
    }

    @Transactional
    public CatalogoCuenta create(CatalogoCuenta cuenta) {
        if (findByCodigo(cuenta.getCodigo()).isPresent()) {
            throw new BusinessException("DUPLICATE_CODE", "Ya existe una cuenta con el código: " + cuenta.getCodigo());
        }
        cuenta.setId(UUID.randomUUID());
        persistEntity(cuenta);
        return cuenta;
    }

    @Transactional
    public CatalogoCuenta update(UUID id, CatalogoCuenta cuenta) {
        CatalogoCuenta existing = findById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("CatalogoCuenta", id);
        }
        
        Optional<CatalogoCuenta> byCode = findByCodigo(cuenta.getCodigo());
        if (byCode.isPresent() && !byCode.get().getId().equals(id)) {
            throw new BusinessException("DUPLICATE_CODE", "Ya existe una cuenta con el código: " + cuenta.getCodigo());
        }
        
        existing.setCodigo(cuenta.getCodigo());
        existing.setNombre(cuenta.getNombre());
        existing.setTipo(cuenta.getTipo());
        existing.setNivel(cuenta.getNivel());
        existing.setDescripcion(cuenta.getDescripcion());
        existing.setCuentaPadre(cuenta.getCuentaPadre());
        existing.setUsuarioModificacion(cuenta.getUsuarioModificacion());
        
        return updateEntity(existing);
    }

    @Transactional
    public void softDelete(UUID id) {
        CatalogoCuenta existing = findById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("CatalogoCuenta", id);
        }
        existing.setActivo(false);
        updateEntity(existing);
    }
}
