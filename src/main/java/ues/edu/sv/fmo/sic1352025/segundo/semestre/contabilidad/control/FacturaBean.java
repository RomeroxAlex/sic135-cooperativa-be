package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.VentaDiariaDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.Factura;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.ItemFactura;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.BusinessException;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ResourceNotFoundException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Stateless
@LocalBean
public class FacturaBean extends AbstractDataPersist<Factura> implements Serializable {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;

    public FacturaBean() {
        super(Factura.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<Factura> findAllOrdered() {
        TypedQuery<Factura> query = em.createNamedQuery("Factura.findAll", Factura.class);
        return query.getResultList();
    }

    public List<Factura> findPaginated(int page, int size) {
        TypedQuery<Factura> query = em.createNamedQuery("Factura.findAll", Factura.class);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    public long count() {
        TypedQuery<Long> query = em.createNamedQuery("Factura.countAll", Long.class);
        return query.getSingleResult();
    }

    public String getNextNumeroFactura() {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(f) + 1 FROM Factura f", Long.class);
        Long numero = query.getSingleResult();
        return String.format("FAC-%s-%06d", 
            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM")), numero);
    }

    @Transactional
    public Factura create(Factura factura) {
        factura.setId(UUID.randomUUID());
        factura.setNumeroFactura(getNextNumeroFactura());
        factura.setEstado("BORRADOR");
        
        for (ItemFactura item : factura.getItems()) {
            item.setId(UUID.randomUUID());
            item.setFactura(factura);
            if (item.getCantidad() != null && item.getPrecioUnitario() != null) {
                item.setSubtotal(item.getCantidad().multiply(item.getPrecioUnitario()));
            }
        }
        
        factura.recalcularTotales();
        persistEntity(factura);
        return factura;
    }

    @Transactional
    public Factura update(UUID id, Factura factura) {
        Factura existing = findById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Factura", id);
        }
        
        if ("EMITIDA".equals(existing.getEstado()) || "PAGADA".equals(existing.getEstado())) {
            throw new BusinessException("LOCKED", "No se puede modificar una factura emitida o pagada");
        }
        
        existing.setClienteId(factura.getClienteId());
        existing.setClienteNombre(factura.getClienteNombre());
        existing.setClienteNit(factura.getClienteNit());
        existing.setFecha(factura.getFecha());
        existing.setObservaciones(factura.getObservaciones());
        existing.setUsuarioModificacion(factura.getUsuarioModificacion());
        
        existing.getItems().clear();
        for (ItemFactura item : factura.getItems()) {
            item.setId(UUID.randomUUID());
            existing.addItem(item);
        }
        
        existing.recalcularTotales();
        return updateEntity(existing);
    }

    @Transactional
    public Factura emitir(UUID id) {
        Factura factura = findById(id);
        if (factura == null) {
            throw new ResourceNotFoundException("Factura", id);
        }
        
        if (!"BORRADOR".equals(factura.getEstado())) {
            throw new BusinessException("INVALID_STATE", "Solo se pueden emitir facturas en estado BORRADOR");
        }
        
        factura.setEstado("EMITIDA");
        return updateEntity(factura);
    }

    @Transactional
    public Factura anular(UUID id) {
        Factura factura = findById(id);
        if (factura == null) {
            throw new ResourceNotFoundException("Factura", id);
        }
        
        factura.setEstado("ANULADA");
        return updateEntity(factura);
    }

    public VentaDiariaDto getReporteVentasDiarias(LocalDate fecha) {
        TypedQuery<Object[]> query = em.createQuery(
            "SELECT COUNT(f), COALESCE(SUM(f.total), 0), COALESCE(SUM(f.impuestos), 0) " +
            "FROM Factura f WHERE f.fecha = :fecha AND f.estado IN ('EMITIDA', 'PAGADA')", 
            Object[].class);
        query.setParameter("fecha", fecha);
        
        Object[] result = query.getSingleResult();
        Long totalFacturas = (Long) result[0];
        BigDecimal totalVentas = (BigDecimal) result[1];
        BigDecimal totalImpuestos = (BigDecimal) result[2];
        
        BigDecimal promedio = totalFacturas > 0 
            ? totalVentas.divide(BigDecimal.valueOf(totalFacturas), 2, RoundingMode.HALF_UP)
            : BigDecimal.ZERO;
        
        return new VentaDiariaDto(fecha, totalFacturas, totalVentas, totalImpuestos, promedio);
    }

    public List<Factura> findByFecha(LocalDate fecha) {
        TypedQuery<Factura> query = em.createNamedQuery("Factura.findByFecha", Factura.class);
        query.setParameter("fecha", fecha);
        return query.getResultList();
    }
}
