package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CatalogoCuenta;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("CatalogoCuentaBean Tests")
@ExtendWith(MockitoExtension.class)
class CatalogoCuentaBeanTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<CatalogoCuenta> cuentaQuery;

    @Mock
    private TypedQuery<Long> countQuery;

    private CatalogoCuentaBean catalogoCuentaBean;

    @BeforeEach
    void setUp() throws Exception {
        catalogoCuentaBean = new CatalogoCuentaBean();
        // Use reflection to inject the mock EntityManager
        Field emField = CatalogoCuentaBean.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(catalogoCuentaBean, em);
    }

    @Test
    @DisplayName("Should find all active accounts")
    void findAllActive_ShouldReturnActiveAccounts() {
        // Arrange
        CatalogoCuenta cuenta1 = createCuenta("1101", "Caja", "ACTIVO");
        CatalogoCuenta cuenta2 = createCuenta("1102", "Bancos", "ACTIVO");
        List<CatalogoCuenta> cuentas = Arrays.asList(cuenta1, cuenta2);

        when(em.createNamedQuery("CatalogoCuenta.findAll", CatalogoCuenta.class)).thenReturn(cuentaQuery);
        when(cuentaQuery.getResultList()).thenReturn(cuentas);

        // Act
        List<CatalogoCuenta> result = catalogoCuentaBean.findAllActive();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(em).createNamedQuery("CatalogoCuenta.findAll", CatalogoCuenta.class);
    }

    @Test
    @DisplayName("Should find accounts by tipo")
    void findByTipo_ShouldReturnAccountsByType() {
        // Arrange
        CatalogoCuenta cuenta1 = createCuenta("1101", "Caja", "ACTIVO");
        List<CatalogoCuenta> cuentas = Arrays.asList(cuenta1);

        when(em.createNamedQuery("CatalogoCuenta.findByTipo", CatalogoCuenta.class)).thenReturn(cuentaQuery);
        when(cuentaQuery.setParameter("tipo", "ACTIVO")).thenReturn(cuentaQuery);
        when(cuentaQuery.getResultList()).thenReturn(cuentas);

        // Act
        List<CatalogoCuenta> result = catalogoCuentaBean.findByTipo("ACTIVO");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ACTIVO", result.get(0).getTipo());
    }

    @Test
    @DisplayName("Should find account by codigo")
    void findByCodigo_ShouldReturnAccount() {
        // Arrange
        CatalogoCuenta cuenta = createCuenta("1101", "Caja", "ACTIVO");
        List<CatalogoCuenta> cuentas = Arrays.asList(cuenta);

        when(em.createNamedQuery("CatalogoCuenta.findByCodigo", CatalogoCuenta.class)).thenReturn(cuentaQuery);
        when(cuentaQuery.setParameter("codigo", "1101")).thenReturn(cuentaQuery);
        when(cuentaQuery.getResultList()).thenReturn(cuentas);

        // Act
        Optional<CatalogoCuenta> result = catalogoCuentaBean.findByCodigo("1101");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("1101", result.get().getCodigo());
    }

    @Test
    @DisplayName("Should return empty when codigo not found")
    void findByCodigo_ShouldReturnEmptyWhenNotFound() {
        // Arrange
        when(em.createNamedQuery("CatalogoCuenta.findByCodigo", CatalogoCuenta.class)).thenReturn(cuentaQuery);
        when(cuentaQuery.setParameter("codigo", "9999")).thenReturn(cuentaQuery);
        when(cuentaQuery.getResultList()).thenReturn(Arrays.asList());

        // Act
        Optional<CatalogoCuenta> result = catalogoCuentaBean.findByCodigo("9999");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Should count all accounts")
    void count_ShouldReturnTotal() {
        // Arrange
        when(em.createNamedQuery("CatalogoCuenta.countAll", Long.class)).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(10L);

        // Act
        long result = catalogoCuentaBean.count();

        // Assert
        assertEquals(10L, result);
    }

    private CatalogoCuenta createCuenta(String codigo, String nombre, String tipo) {
        CatalogoCuenta cuenta = new CatalogoCuenta();
        cuenta.setId(UUID.randomUUID());
        cuenta.setCodigo(codigo);
        cuenta.setNombre(nombre);
        cuenta.setTipo(tipo);
        cuenta.setActivo(true);
        return cuenta;
    }
}
