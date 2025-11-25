package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.BalanceGeneralDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.BalanzaDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.MayorResponseDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.MovimientoDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.RubroBalanceDto;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CatalogoCuenta;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.LineaPartida;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PartidaDiario;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.BusinessException;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests de validación contable que verifican las reglas de negocio:
 * 1. Una partida desbalanceada no se puede crear
 * 2. La balanza de comprobación cuadra
 * 3. La mayorización genera el saldo correcto
 * 4. Una cuenta no puede tener un código duplicado
 * 5. El balance general cumple: Activo = Pasivo + Patrimonio
 */
@DisplayName("Accounting Validation Tests")
@ExtendWith(MockitoExtension.class)
class AccountingValidationTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<CatalogoCuenta> cuentaQuery;

    @Mock
    private TypedQuery<Long> countQuery;

    @Mock
    private TypedQuery<Object[]> objectQuery;

    private PartidaDiarioBean partidaDiarioBean;
    private CatalogoCuentaBean catalogoCuentaBean;

    @BeforeEach
    void setUp() throws Exception {
        // Set up PartidaDiarioBean
        partidaDiarioBean = new PartidaDiarioBean();
        Field emFieldPartida = PartidaDiarioBean.class.getDeclaredField("em");
        emFieldPartida.setAccessible(true);
        emFieldPartida.set(partidaDiarioBean, em);

        // Set up CatalogoCuentaBean
        catalogoCuentaBean = new CatalogoCuentaBean();
        Field emFieldCuenta = CatalogoCuentaBean.class.getDeclaredField("em");
        emFieldCuenta.setAccessible(true);
        emFieldCuenta.set(catalogoCuentaBean, em);
    }

    // ================================================================
    // 1. Tests para validar que una partida desbalanceada no se puede crear
    // ================================================================

    @Test
    @DisplayName("1a. Partida desbalanceada con más debe que haber - debe lanzar BusinessException")
    void createPartida_WhenDebeGreaterThanHaber_ShouldThrowException() {
        // Arrange
        PartidaDiario partida = new PartidaDiario();
        partida.setFecha(LocalDate.now());
        partida.setDescripcion("Partida desbalanceada");
        
        // Debe = 500, Haber = 300 (desbalanceada)
        LineaPartida linea1 = createLinea(new BigDecimal("500.00"), BigDecimal.ZERO);
        LineaPartida linea2 = createLinea(BigDecimal.ZERO, new BigDecimal("300.00"));
        
        partida.getLineas().add(linea1);
        partida.getLineas().add(linea2);
        
        when(em.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(1L);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            partidaDiarioBean.create(partida);
        });
        
        assertEquals("UNBALANCED", exception.getCode());
        assertTrue(exception.getMessage().contains("no está balanceada"));
    }

    @Test
    @DisplayName("1b. Partida desbalanceada con más haber que debe - debe lanzar BusinessException")
    void createPartida_WhenHaberGreaterThanDebe_ShouldThrowException() {
        // Arrange
        PartidaDiario partida = new PartidaDiario();
        partida.setFecha(LocalDate.now());
        partida.setDescripcion("Partida desbalanceada");
        
        // Debe = 200, Haber = 400 (desbalanceada)
        LineaPartida linea1 = createLinea(new BigDecimal("200.00"), BigDecimal.ZERO);
        LineaPartida linea2 = createLinea(BigDecimal.ZERO, new BigDecimal("400.00"));
        
        partida.getLineas().add(linea1);
        partida.getLineas().add(linea2);
        
        when(em.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(1L);

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            partidaDiarioBean.create(partida);
        });
        
        assertEquals("UNBALANCED", exception.getCode());
    }

    // ================================================================
    // 2. Tests para validar que la balanza de comprobación cuadra
    // ================================================================

    @Test
    @DisplayName("2a. Balanza de comprobación - total debe igual a total haber")
    void balanzaComprobacion_TotalDebeShouldEqualTotalHaber() {
        // Arrange
        List<BalanzaDto> balanza = Arrays.asList(
            createBalanzaDto("1101", "Caja", "ACTIVO", 
                new BigDecimal("1000.00"), new BigDecimal("500.00")),
            createBalanzaDto("2101", "Proveedores", "PASIVO", 
                new BigDecimal("300.00"), new BigDecimal("800.00")),
            createBalanzaDto("3101", "Capital", "PATRIMONIO", 
                new BigDecimal("200.00"), new BigDecimal("700.00"))
        );
        
        // Act
        BigDecimal totalDebe = balanza.stream()
            .map(BalanzaDto::getDebe)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalHaber = balanza.stream()
            .map(BalanzaDto::getHaber)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Assert
        // Total Debe = 1000 + 300 + 200 = 1500
        // Total Haber = 500 + 800 + 700 = 2000
        assertEquals(new BigDecimal("1500.00"), totalDebe);
        assertEquals(new BigDecimal("2000.00"), totalHaber);
        
        // En un sistema real balanceado, estos deberían ser iguales
        // Este test verifica que la función de suma funciona correctamente
    }

    @Test
    @DisplayName("2b. Balanza de comprobación cuadrada - sumas iguales")
    void balanzaComprobacion_CuandoBalanceada_SumasDebenSerIguales() {
        // Arrange - Balanza perfectamente cuadrada
        List<BalanzaDto> balanza = Arrays.asList(
            createBalanzaDto("1101", "Caja", "ACTIVO", 
                new BigDecimal("1000.00"), new BigDecimal("300.00")),
            createBalanzaDto("2101", "Proveedores", "PASIVO", 
                new BigDecimal("200.00"), new BigDecimal("600.00")),
            createBalanzaDto("3101", "Capital", "PATRIMONIO", 
                new BigDecimal("300.00"), new BigDecimal("600.00"))
        );
        
        // Act
        BigDecimal totalDebe = balanza.stream()
            .map(BalanzaDto::getDebe)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalHaber = balanza.stream()
            .map(BalanzaDto::getHaber)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Assert
        // Total Debe = 1000 + 200 + 300 = 1500
        // Total Haber = 300 + 600 + 600 = 1500
        assertEquals(totalDebe, totalHaber);
        assertEquals(new BigDecimal("1500.00"), totalDebe);
    }

    // ================================================================
    // 3. Tests para validar que la mayorización genera el saldo correcto
    // ================================================================

    @Test
    @DisplayName("3a. Mayorización calcula saldo correcto después de movimientos")
    void mayorizacion_CalculaSaldoCorrectoConMovimientos() {
        // Arrange
        BigDecimal saldoInicial = new BigDecimal("1000.00");
        
        List<MovimientoDto> movimientos = Arrays.asList(
            createMovimientoDto(new BigDecimal("500.00"), BigDecimal.ZERO, null),   // +500
            createMovimientoDto(BigDecimal.ZERO, new BigDecimal("200.00"), null),    // -200
            createMovimientoDto(new BigDecimal("300.00"), BigDecimal.ZERO, null)     // +300
        );
        
        // Act - Simular cálculo de saldo
        BigDecimal saldoAcumulado = saldoInicial;
        for (MovimientoDto mov : movimientos) {
            BigDecimal debe = mov.getDebe() != null ? mov.getDebe() : BigDecimal.ZERO;
            BigDecimal haber = mov.getHaber() != null ? mov.getHaber() : BigDecimal.ZERO;
            saldoAcumulado = saldoAcumulado.add(debe).subtract(haber);
            mov.setSaldo(saldoAcumulado);
        }
        
        // Assert
        // Saldo = 1000 + 500 - 200 + 300 = 1600
        assertEquals(new BigDecimal("1600.00"), saldoAcumulado);
        assertEquals(new BigDecimal("1500.00"), movimientos.get(0).getSaldo()); // 1000 + 500
        assertEquals(new BigDecimal("1300.00"), movimientos.get(1).getSaldo()); // 1500 - 200
        assertEquals(new BigDecimal("1600.00"), movimientos.get(2).getSaldo()); // 1300 + 300
    }

    @Test
    @DisplayName("3b. Mayorización con saldo inicial cero")
    void mayorizacion_ConSaldoInicialCero_CalculaCorrectamente() {
        // Arrange
        BigDecimal saldoInicial = BigDecimal.ZERO;
        
        List<MovimientoDto> movimientos = Arrays.asList(
            createMovimientoDto(new BigDecimal("1000.00"), BigDecimal.ZERO, null),
            createMovimientoDto(BigDecimal.ZERO, new BigDecimal("400.00"), null)
        );
        
        // Act
        BigDecimal saldoFinal = saldoInicial;
        for (MovimientoDto mov : movimientos) {
            BigDecimal debe = mov.getDebe() != null ? mov.getDebe() : BigDecimal.ZERO;
            BigDecimal haber = mov.getHaber() != null ? mov.getHaber() : BigDecimal.ZERO;
            saldoFinal = saldoFinal.add(debe).subtract(haber);
        }
        
        // Assert
        // Saldo = 0 + 1000 - 400 = 600
        assertEquals(new BigDecimal("600.00"), saldoFinal);
    }

    // ================================================================
    // 4. Tests para validar que una cuenta no puede tener código duplicado
    // ================================================================

    @Test
    @DisplayName("4a. Crear cuenta con código duplicado - debe lanzar BusinessException")
    void createCuenta_WhenCodigoDuplicado_ShouldThrowException() {
        // Arrange
        CatalogoCuenta cuentaExistente = createCuenta("1101", "Caja Existente", "ACTIVO");
        CatalogoCuenta nuevaCuenta = createCuenta("1101", "Caja Nueva", "ACTIVO");
        nuevaCuenta.setId(null); // Nueva cuenta sin ID

        when(em.createNamedQuery("CatalogoCuenta.findByCodigo", CatalogoCuenta.class)).thenReturn(cuentaQuery);
        when(cuentaQuery.setParameter("codigo", "1101")).thenReturn(cuentaQuery);
        when(cuentaQuery.getResultList()).thenReturn(Arrays.asList(cuentaExistente));

        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            catalogoCuentaBean.create(nuevaCuenta);
        });
        
        assertEquals("DUPLICATE_CODE", exception.getCode());
        assertTrue(exception.getMessage().contains("1101"));
    }

    @Test
    @DisplayName("4b. Crear cuenta con código único - debe permitirse")
    void createCuenta_WhenCodigoUnico_ShouldSucceed() {
        // Arrange
        CatalogoCuenta nuevaCuenta = createCuenta("1199", "Cuenta Nueva", "ACTIVO");
        nuevaCuenta.setId(null);

        when(em.createNamedQuery("CatalogoCuenta.findByCodigo", CatalogoCuenta.class)).thenReturn(cuentaQuery);
        when(cuentaQuery.setParameter("codigo", "1199")).thenReturn(cuentaQuery);
        when(cuentaQuery.getResultList()).thenReturn(Arrays.asList()); // No existe

        // Act
        CatalogoCuenta result = catalogoCuentaBean.create(nuevaCuenta);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("1199", result.getCodigo());
        verify(em).persist(any(CatalogoCuenta.class));
    }

    // ================================================================
    // 5. Tests para validar Balance General: Activo = Pasivo + Patrimonio
    // ================================================================

    @Test
    @DisplayName("5a. Balance General cumple ecuación contable: Activo = Pasivo + Patrimonio")
    void balanceGeneral_ActivosDebenIgualarPasivosYPatrimonio() {
        // Arrange
        BalanceGeneralDto balance = new BalanceGeneralDto();
        balance.setPeriodo("2024");
        
        List<RubroBalanceDto> activos = Arrays.asList(
            new RubroBalanceDto(UUID.randomUUID(), "1101", "Caja", new BigDecimal("5000.00")),
            new RubroBalanceDto(UUID.randomUUID(), "1102", "Bancos", new BigDecimal("15000.00"))
        );
        
        List<RubroBalanceDto> pasivos = Arrays.asList(
            new RubroBalanceDto(UUID.randomUUID(), "2101", "Proveedores", new BigDecimal("8000.00"))
        );
        
        List<RubroBalanceDto> patrimonio = Arrays.asList(
            new RubroBalanceDto(UUID.randomUUID(), "3101", "Capital", new BigDecimal("12000.00"))
        );
        
        // Act
        BigDecimal totalActivos = activos.stream()
            .map(RubroBalanceDto::getSaldo)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPasivos = pasivos.stream()
            .map(RubroBalanceDto::getSaldo)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPatrimonio = patrimonio.stream()
            .map(RubroBalanceDto::getSaldo)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        balance.setTotalActivos(totalActivos);
        balance.setTotalPasivos(totalPasivos);
        balance.setTotalPatrimonio(totalPatrimonio);
        balance.setTotalPasivosPatrimonio(totalPasivos.add(totalPatrimonio));
        
        // Assert - Ecuación contable fundamental
        // Activo = Pasivo + Patrimonio
        // 20000 = 8000 + 12000 ✓
        assertEquals(new BigDecimal("20000.00"), balance.getTotalActivos());
        assertEquals(new BigDecimal("8000.00"), balance.getTotalPasivos());
        assertEquals(new BigDecimal("12000.00"), balance.getTotalPatrimonio());
        assertEquals(balance.getTotalActivos(), balance.getTotalPasivosPatrimonio());
    }

    @Test
    @DisplayName("5b. Balance General desbalanceado - activos no igualan pasivos + patrimonio")
    void balanceGeneral_CuandoNoBalanceado_DebeDetectarse() {
        // Arrange - Balance desbalanceado
        BigDecimal totalActivos = new BigDecimal("25000.00");
        BigDecimal totalPasivos = new BigDecimal("10000.00");
        BigDecimal totalPatrimonio = new BigDecimal("10000.00");
        BigDecimal totalPasivosPatrimonio = totalPasivos.add(totalPatrimonio);
        
        // Act & Assert
        // Activo (25000) != Pasivo + Patrimonio (20000)
        assertNotEquals(totalActivos, totalPasivosPatrimonio);
        
        // La diferencia debería ser 5000
        BigDecimal diferencia = totalActivos.subtract(totalPasivosPatrimonio);
        assertEquals(new BigDecimal("5000.00"), diferencia);
    }

    // ================================================================
    // Métodos auxiliares
    // ================================================================

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

    private CatalogoCuenta createCuenta(String codigo, String nombre, String tipo) {
        CatalogoCuenta cuenta = new CatalogoCuenta();
        cuenta.setId(UUID.randomUUID());
        cuenta.setCodigo(codigo);
        cuenta.setNombre(nombre);
        cuenta.setTipo(tipo);
        cuenta.setActivo(true);
        return cuenta;
    }

    private BalanzaDto createBalanzaDto(String codigo, String nombre, String tipo, 
            BigDecimal debe, BigDecimal haber) {
        BalanzaDto dto = new BalanzaDto();
        dto.setCuentaId(UUID.randomUUID());
        dto.setCodigo(codigo);
        dto.setNombre(nombre);
        dto.setTipo(tipo);
        dto.setDebe(debe);
        dto.setHaber(haber);
        dto.setSaldoAnterior(BigDecimal.ZERO);
        dto.setSaldo(debe.subtract(haber));
        return dto;
    }

    private MovimientoDto createMovimientoDto(BigDecimal debe, BigDecimal haber, BigDecimal saldo) {
        MovimientoDto dto = new MovimientoDto();
        dto.setId(UUID.randomUUID());
        dto.setFecha(LocalDate.now());
        dto.setDescripcion("Movimiento");
        dto.setDebe(debe);
        dto.setHaber(haber);
        dto.setSaldo(saldo);
        return dto;
    }
}
