package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.Auditoria;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Stateless
@LocalBean
public class AuditoriaBean extends AbstractDataPersist<Auditoria> implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;

    public AuditoriaBean() {
        super(Auditoria.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Auditoria> findAllOrdered() {
        TypedQuery<Auditoria> query = em.createNamedQuery("Auditoria.findAll", Auditoria.class);
        return query.getResultList();
    }

    public List<Auditoria> findByUsuario(String usuario) {
        TypedQuery<Auditoria> query = em.createNamedQuery("Auditoria.findByUsuario", Auditoria.class);
        query.setParameter("usuario", usuario);
        return query.getResultList();
    }

    public List<Auditoria> findByEntidad(String entidad) {
        TypedQuery<Auditoria> query = em.createNamedQuery("Auditoria.findByEntidad", Auditoria.class);
        query.setParameter("entidad", entidad);
        return query.getResultList();
    }

    public List<Auditoria> findPaginated(int page, int size, String usuario, String entidad) {
        StringBuilder jpql = new StringBuilder("SELECT a FROM Auditoria a WHERE 1=1");
        
        if (usuario != null && !usuario.isEmpty()) {
            jpql.append(" AND a.usuario = :usuario");
        }
        if (entidad != null && !entidad.isEmpty()) {
            jpql.append(" AND a.entidad = :entidad");
        }
        jpql.append(" ORDER BY a.fechaAccion DESC");
        
        TypedQuery<Auditoria> query = em.createQuery(jpql.toString(), Auditoria.class);
        
        if (usuario != null && !usuario.isEmpty()) {
            query.setParameter("usuario", usuario);
        }
        if (entidad != null && !entidad.isEmpty()) {
            query.setParameter("entidad", entidad);
        }
        
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Transactional
    public Auditoria registrar(String entidad, String entidadId, String accion, 
                                String usuario, String datosAnteriores, String datosNuevos,
                                String ipAddress, String detalle) {
        Auditoria auditoria = new Auditoria();
        auditoria.setId(UUID.randomUUID());
        auditoria.setEntidad(entidad);
        auditoria.setEntidadId(entidadId);
        auditoria.setAccion(accion);
        auditoria.setUsuario(usuario);
        auditoria.setDatosAnteriores(datosAnteriores);
        auditoria.setDatosNuevos(datosNuevos);
        auditoria.setIpAddress(ipAddress);
        auditoria.setDetalle(detalle);
        
        persistEntity(auditoria);
        return auditoria;
    }
}
