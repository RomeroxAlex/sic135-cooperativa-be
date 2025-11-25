package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.ManualCuenta;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ResourceNotFoundException;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Stateless
@LocalBean
public class ManualCuentaBean extends AbstractDataPersist<ManualCuenta> implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;

    public ManualCuentaBean() {
        super(ManualCuenta.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<ManualCuenta> findAllActive() {
        TypedQuery<ManualCuenta> query = em.createNamedQuery("ManualCuenta.findAll", ManualCuenta.class);
        return query.getResultList();
    }

    @Transactional
    public ManualCuenta create(ManualCuenta manual) {
        manual.setId(UUID.randomUUID());
        persistEntity(manual);
        return manual;
    }

    @Transactional
    public ManualCuenta update(UUID id, ManualCuenta manual) {
        ManualCuenta existing = findById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("ManualCuenta", id);
        }
        
        existing.setTitulo(manual.getTitulo());
        existing.setContenido(manual.getContenido());
        existing.setVersion(manual.getVersion());
        existing.setUsuarioModificacion(manual.getUsuarioModificacion());
        
        return updateEntity(existing);
    }
}
