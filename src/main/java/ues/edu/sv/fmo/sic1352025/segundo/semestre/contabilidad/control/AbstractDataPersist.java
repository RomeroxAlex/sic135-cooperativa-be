package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

/**
 *
 * @author jordi
 */
public abstract class AbstractDataPersist<T> {

    public abstract EntityManager getEntityManager();

    Class tipoDatos;

    public AbstractDataPersist(Class tipoDatos) {
        this.tipoDatos = tipoDatos;
    }

    public void persistEntity(T entity) throws IllegalStateException, IllegalArgumentException {
        try {
            EntityManager em = getEntityManager();
            if (em == null) {
                throw new IllegalStateException("Error al Acceder al Entitity Manager");
            } else if (entity == null) {
                throw new IllegalArgumentException("La entidad no puede Ser Null");
            }
            em.persist(entity);
        } catch (Exception ex) {
            throw new IllegalStateException("Error al persistir", ex);
        }

    }

    public T updateEntity(T entity) {
        try {
            EntityManager em = getEntityManager();
            if (em == null) {
                throw new IllegalStateException("Error al Acceder al Entitity Manager");
            } else if (entity == null) {
                throw new IllegalArgumentException("La entidad no puede Ser Null");
            }
            T updatedEntity = em.merge(entity);
            return updatedEntity;
        } catch (Exception ex) {
            throw new IllegalStateException("Error al Actualizar Entidad", ex);

        }
    }

    public void deleteEntity(T entity) throws IllegalStateException, IllegalArgumentException {
        try {
            EntityManager em = getEntityManager();
            if (em == null) {
                throw new IllegalStateException("Error al Acceder al Entitity Manager");
            } else if (entity == null) {
                throw new IllegalArgumentException("La entidad no puede Ser Null");
            }
            if (!em.contains(entity)) {
                em.merge(entity);
            }
            em.remove(entity);
        } catch (Exception ex) {
            throw new IllegalStateException("Error al Remover Entidad", ex);

        }
    }

    public List<T> findRange(int first, int max) throws IllegalArgumentException, IllegalStateException {
        if (first < 0 || max < 0) {
            throw new IllegalArgumentException("Parametro no válido: first o max");
        }

        try {
            EntityManager em = getEntityManager();
            if (em == null) {
                throw new IllegalStateException("Error al acceder al repositorio");
            }

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(tipoDatos); // Es así porque recibe Tipo Class
            Root<T> raiz = cq.from(tipoDatos);
            cq.select(raiz);
            TypedQuery<T> query = em.createQuery(cq);
            query.setFirstResult(first);
            query.setMaxResults(max);

            return query.getResultList();
        } catch (Exception ex) {
            throw new IllegalStateException("Error al Ejercutar Query", ex);
        }
    }

    public T findById(final Object id) throws IllegalArgumentException, IllegalStateException {

        EntityManager em = null;
        if (id == null) {
            throw new IllegalArgumentException("El param id no puede ser null");
        }
        try {
            em = getEntityManager();
            if (em == null) {
                throw new IllegalStateException("El em no puede ser null");
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Error al acceder al obtener Leer de Tipo", ex);
        }

        return (T) em.find(tipoDatos, id);
    }

    /**
     *
     * @return Todos los registros en una tabla
     * FORMA JPQL -> SELECT raiz from tipoDatos raiz- tipoDatos sería el origen, igual la raíz
     */
    public List<T> findAll() {
        EntityManager em = getEntityManager();
        if(em == null){
            throw new IllegalStateException("El EntityManager no puede ser null");
        }
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(tipoDatos);
            Root<T> raiz = cq.from(tipoDatos);
            cq.select(raiz);
            
            // Acá solo la ejecutamos y asignamos a su tipo de datos
            TypedQuery<T> query = em.createQuery(cq);
            return query.getResultList();
        } catch (Exception ex) {
            throw new IllegalStateException("Error al acceder al obtener Leer de Tipo", ex);

        }
    }

}
