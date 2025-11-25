package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CatalogoCuenta;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.LineaPartida;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PartidaDiario;

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

@DisplayName("PartidaDiarioBean Tests")
@ExtendWith(MockitoExtension.class)
class PartidaDiarioBeanTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<PartidaDiario> partidaQuery;

    @Mock
    private TypedQuery<Long> countQuery;

    private PartidaDiarioBean partidaDiarioBean;

    @BeforeEach
    void setUp() throws Exception {
        partidaDiarioBean = new PartidaDiarioBean();
        // Use reflection to inject the mock EntityManager
        Field emField = PartidaDiarioBean.class.getDeclaredField("em");
        emField.setAccessible(true);
        emField.set(partidaDiarioBean, em);
    }

    @Test
    @DisplayName("Should validate that partida is balanced")
    void partidaBalanceada_ShouldBeBalanced() {
        // Arrange
        PartidaDiario partida = new PartidaDiario();
        partida.setFecha(LocalDate.now());
        
        LineaPartida linea1 = createLinea(new BigDecimal("100.00"), BigDecimal.ZERO);
        LineaPartida linea2 = createLinea(BigDecimal.ZERO, new BigDecimal("100.00"));
        
        partida.getLineas().add(linea1);
        partida.getLineas().add(linea2);
        partida.recalcularTotales();

        // Assert
        assertTrue(partida.estaBalanceada());
        assertEquals(new BigDecimal("100.00"), partida.getTotalDebe());
        assertEquals(new BigDecimal("100.00"), partida.getTotalHaber());
    }

    @Test
    @DisplayName("Should detect unbalanced partida")
    void partidaNoBalanceada_ShouldNotBeBalanced() {
        // Arrange
        PartidaDiario partida = new PartidaDiario();
        partida.setFecha(LocalDate.now());
        
        LineaPartida linea1 = createLinea(new BigDecimal("100.00"), BigDecimal.ZERO);
        LineaPartida linea2 = createLinea(BigDecimal.ZERO, new BigDecimal("50.00"));
        
        partida.getLineas().add(linea1);
        partida.getLineas().add(linea2);
        partida.recalcularTotales();

        // Assert
        assertFalse(partida.estaBalanceada());
    }

    @Test
    @DisplayName("Should find all partidas ordered")
    void findAllOrdered_ShouldReturnPartidas() {
        // Arrange
        PartidaDiario partida1 = createPartida(1L, LocalDate.now());
        List<PartidaDiario> partidas = Arrays.asList(partida1);

        when(em.createNamedQuery("PartidaDiario.findAll", PartidaDiario.class)).thenReturn(partidaQuery);
        when(partidaQuery.getResultList()).thenReturn(partidas);

        // Act
        List<PartidaDiario> result = partidaDiarioBean.findAllOrdered();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should find partidas by estado")
    void findByEstado_ShouldReturnPartidas() {
        // Arrange
        PartidaDiario partida1 = createPartida(1L, LocalDate.now());
        partida1.setEstado("BORRADOR");
        List<PartidaDiario> partidas = Arrays.asList(partida1);

        when(em.createNamedQuery("PartidaDiario.findByEstado", PartidaDiario.class)).thenReturn(partidaQuery);
        when(partidaQuery.setParameter("estado", "BORRADOR")).thenReturn(partidaQuery);
        when(partidaQuery.getResultList()).thenReturn(partidas);

        // Act
        List<PartidaDiario> result = partidaDiarioBean.findByEstado("BORRADOR");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("BORRADOR", result.get(0).getEstado());
    }

    @Test
    @DisplayName("Should get next numero partida")
    void getNextNumeroPartida_ShouldReturnNext() {
        // Arrange
        when(em.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(5L);

        // Act
        Long result = partidaDiarioBean.getNextNumeroPartida();

        // Assert
        assertEquals(5L, result);
    }

    private LineaPartida createLinea(BigDecimal debe, BigDecimal haber) {
        LineaPartida linea = new LineaPartida();
        linea.setId(UUID.randomUUID());
        linea.setDebe(debe);
        linea.setHaber(haber);
        
        CatalogoCuenta cuenta = new CatalogoCuenta();
        cuenta.setId(UUID.randomUUID());
        cuenta.setCodigo("1101");
        cuenta.setNombre("Cuenta");
        linea.setCuenta(cuenta);
        
        return linea;
    }

    private PartidaDiario createPartida(Long numero, LocalDate fecha) {
        PartidaDiario partida = new PartidaDiario();
        partida.setId(UUID.randomUUID());
        partida.setNumeroPartida(numero);
        partida.setFecha(fecha);
        partida.setEstado("BORRADOR");
        return partida;
    }
}
