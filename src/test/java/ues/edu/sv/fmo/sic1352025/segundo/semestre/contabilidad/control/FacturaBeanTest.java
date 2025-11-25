package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.Factura;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.ItemFactura;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("FacturaBean Tests")
@ExtendWith(MockitoExtension.class)
class FacturaBeanTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<Factura> facturaQuery;

    @Mock
    private TypedQuery<Long> countQuery;

    private FacturaBean facturaBean;

    @BeforeEach
    void setUp() throws Exception {
        facturaBean = new FacturaBean();
        // Use reflection to inject the mock EntityManager
        Field emField = FacturaBean.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(facturaBean, em);
    }

    @Test
    @DisplayName("Should calculate totals correctly")
    void recalcularTotales_ShouldCalculateCorrectly() {
        // Arrange
        Factura factura = new Factura();
        factura.setFecha(LocalDate.now());
        
        ItemFactura item1 = createItem("Producto 1", new BigDecimal("2"), new BigDecimal("50.00"));
        ItemFactura item2 = createItem("Producto 2", new BigDecimal("3"), new BigDecimal("30.00"));
        
        factura.addItem(item1);
        factura.addItem(item2);

        // Assert
        // Subtotal = (2 * 50) + (3 * 30) = 100 + 90 = 190
        assertEquals(0, new BigDecimal("190.00").compareTo(factura.getSubtotal()));
        // Impuestos = 190 * 0.13 = 24.70
        assertEquals(0, new BigDecimal("24.70").compareTo(factura.getImpuestos()));
        // Total = 190 + 24.70 = 214.70
        assertEquals(0, new BigDecimal("214.70").compareTo(factura.getTotal()));
    }

    @Test
    @DisplayName("Should find all facturas ordered")
    void findAllOrdered_ShouldReturnFacturas() {
        // Arrange
        Factura factura1 = createFactura("FAC-001", LocalDate.now());
        List<Factura> facturas = Arrays.asList(factura1);

        when(em.createNamedQuery("Factura.findAll", Factura.class)).thenReturn(facturaQuery);
        when(facturaQuery.getResultList()).thenReturn(facturas);

        // Act
        List<Factura> result = facturaBean.findAllOrdered();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should find facturas by fecha")
    void findByFecha_ShouldReturnFacturas() {
        // Arrange
        LocalDate fecha = LocalDate.now();
        Factura factura1 = createFactura("FAC-001", fecha);
        List<Factura> facturas = Arrays.asList(factura1);

        when(em.createNamedQuery("Factura.findByFecha", Factura.class)).thenReturn(facturaQuery);
        when(facturaQuery.setParameter("fecha", fecha)).thenReturn(facturaQuery);
        when(facturaQuery.getResultList()).thenReturn(facturas);

        // Act
        List<Factura> result = facturaBean.findByFecha(fecha);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should count all facturas")
    void count_ShouldReturnTotal() {
        // Arrange
        when(em.createNamedQuery("Factura.countAll", Long.class)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(5L);

        // Act
        long result = facturaBean.count();

        // Assert
        assertEquals(5L, result);
    }

    private ItemFactura createItem(String descripcion, BigDecimal cantidad, BigDecimal precio) {
        ItemFactura item = new ItemFactura();
        item.setId(UUID.randomUUID());
        item.setDescripcion(descripcion);
        item.setCantidad(cantidad);
        item.setPrecioUnitario(precio);
        item.setSubtotal(cantidad.multiply(precio));
        return item;
    }

    private Factura createFactura(String numero, LocalDate fecha) {
        Factura factura = new Factura();
        factura.setId(UUID.randomUUID());
        factura.setNumeroFactura(numero);
        factura.setFecha(fecha);
        factura.setEstado("BORRADOR");
        return factura;
    }
}
