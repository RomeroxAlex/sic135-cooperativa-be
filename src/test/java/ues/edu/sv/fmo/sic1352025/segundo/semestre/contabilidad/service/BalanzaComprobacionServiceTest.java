package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.BalanzaComprobacionBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CuentaContableBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.LibroMayorBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.PeriodoContableBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.BalanzaComprobacion;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.LibroMayor;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PeriodoContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ContabilidadException;

/**
 * Unit tests for BalanzaComprobacionService.
 * Tests trial balance generation and balance validation.
 */
@ExtendWith(MockitoExtension.class)
class BalanzaComprobacionServiceTest {

    @Mock
    private BalanzaComprobacionBean balanzaComprobacionBean;
    
    @Mock
    private PeriodoContableBean periodoContableBean;
    
    @Mock
    private CuentaContableBean cuentaContableBean;
    
    @Mock
    private LibroMayorBean libroMayorBean;
    
    @Mock
    private LibroMayorService libroMayorService;
    
    @InjectMocks
    private BalanzaComprobacionService balanzaComprobacionService;
    
    private PeriodoContable periodo;
    private List<CuentaContable> cuentas;
    
    @BeforeEach
    void setUp() {
        periodo = new PeriodoContable(UUID.randomUUID());
        periodo.setNombre("Enero 2025");
        periodo.setActivo(true);
        periodo.setCerrado(false);
        
        cuentas = new ArrayList<>();
        
        // Create test accounts
        CuentaContable caja = new CuentaContable();
        caja.setIdCuentaContable(UUID.randomUUID());
        caja.setNombre("Caja");
        caja.setCodigo(java.math.BigInteger.valueOf(1101));
        caja.setNaturaleza("DEUDORA");
        cuentas.add(caja);
        
        CuentaContable cuentasPorPagar = new CuentaContable();
        cuentasPorPagar.setIdCuentaContable(UUID.randomUUID());
        cuentasPorPagar.setNombre("Cuentas por Pagar");
        cuentasPorPagar.setCodigo(java.math.BigInteger.valueOf(2101));
        cuentasPorPagar.setNaturaleza("ACREEDORA");
        cuentas.add(cuentasPorPagar);
    }
    
    @Test
    @DisplayName("Should generate balanced trial balance")
    void generarBalanzaComprobacion_Balanceada_Success() throws ContabilidadException {
        // Arrange
        UUID idPeriodo = periodo.getIdPeriodoContable();
        
        when(periodoContableBean.findById(idPeriodo)).thenReturn(periodo);
        when(cuentaContableBean.findAll()).thenReturn(cuentas);
        
        // Create balanced ledger entries
        for (CuentaContable cuenta : cuentas) {
            LibroMayor libroMayor = new LibroMayor(UUID.randomUUID());
            libroMayor.setIdCuentaContable(cuenta);
            libroMayor.setSaldoInicial(BigDecimal.ZERO);
            libroMayor.setTotalDebe(new BigDecimal("1000.00"));
            libroMayor.setTotalHaber(new BigDecimal("1000.00"));
            libroMayor.setSaldoFinal(BigDecimal.ZERO);
            
            when(libroMayorBean.findByCuentaYPeriodo(cuenta, periodo)).thenReturn(libroMayor);
        }
        
        // Act
        boolean result = balanzaComprobacionService.generarBalanzaComprobacion(idPeriodo);
        
        // Assert
        assertTrue(result);
        verify(balanzaComprobacionBean, times(2)).persistEntity(any(BalanzaComprobacion.class));
    }
    
    @Test
    @DisplayName("Should throw exception when period not found")
    void generarBalanzaComprobacion_PeriodoNoExiste_ThrowsException() {
        // Arrange
        UUID idPeriodo = UUID.randomUUID();
        when(periodoContableBean.findById(idPeriodo)).thenReturn(null);
        
        // Act & Assert
        ContabilidadException exception = assertThrows(
            ContabilidadException.class,
            () -> balanzaComprobacionService.generarBalanzaComprobacion(idPeriodo)
        );
        
        assertEquals("Período contable no encontrado", exception.getMessage());
    }
    
    @Test
    @DisplayName("Should return correct totals for trial balance")
    void getTotalesBalanza_Success() {
        // Arrange
        UUID idPeriodo = periodo.getIdPeriodoContable();
        List<BalanzaComprobacion> balanzas = createBalancedBalanzas();
        
        when(periodoContableBean.findById(idPeriodo)).thenReturn(periodo);
        when(balanzaComprobacionBean.findByPeriodo(periodo)).thenReturn(balanzas);
        
        // Act
        BigDecimal[] totales = balanzaComprobacionService.getTotalesBalanza(idPeriodo);
        
        // Assert
        assertNotNull(totales);
        assertEquals(6, totales.length);
        // Verify debe = haber for all columns
        assertEquals(totales[0], totales[1]); // Saldo inicial
        assertEquals(totales[2], totales[3]); // Movimientos
        assertEquals(totales[4], totales[5]); // Saldo final
    }
    
    @Test
    @DisplayName("Should verify balance correctly")
    void verificarBalance_Balanceada_ReturnsTrue() {
        // Arrange
        UUID idPeriodo = periodo.getIdPeriodoContable();
        List<BalanzaComprobacion> balanzas = createBalancedBalanzas();
        
        when(periodoContableBean.findById(idPeriodo)).thenReturn(periodo);
        when(balanzaComprobacionBean.findByPeriodo(periodo)).thenReturn(balanzas);
        
        // Act
        boolean result = balanzaComprobacionService.verificarBalance(idPeriodo);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    @DisplayName("Should detect unbalanced trial balance")
    void verificarBalance_NoBalanceada_ReturnsFalse() {
        // Arrange
        UUID idPeriodo = periodo.getIdPeriodoContable();
        List<BalanzaComprobacion> balanzas = createUnbalancedBalanzas();
        
        when(periodoContableBean.findById(idPeriodo)).thenReturn(periodo);
        when(balanzaComprobacionBean.findByPeriodo(periodo)).thenReturn(balanzas);
        
        // Act
        boolean result = balanzaComprobacionService.verificarBalance(idPeriodo);
        
        // Assert
        assertFalse(result);
    }
    
    // Helper methods
    private List<BalanzaComprobacion> createBalancedBalanzas() {
        List<BalanzaComprobacion> balanzas = new ArrayList<>();
        
        // Account 1: Debit balance
        BalanzaComprobacion b1 = new BalanzaComprobacion(UUID.randomUUID());
        b1.setSaldoInicialDebe(new BigDecimal("1000.00"));
        b1.setSaldoInicialHaber(BigDecimal.ZERO);
        b1.setMovimientosDebe(new BigDecimal("500.00"));
        b1.setMovimientosHaber(BigDecimal.ZERO);
        b1.setSaldoFinalDebe(new BigDecimal("1500.00"));
        b1.setSaldoFinalHaber(BigDecimal.ZERO);
        balanzas.add(b1);
        
        // Account 2: Credit balance (same amounts to balance)
        BalanzaComprobacion b2 = new BalanzaComprobacion(UUID.randomUUID());
        b2.setSaldoInicialDebe(BigDecimal.ZERO);
        b2.setSaldoInicialHaber(new BigDecimal("1000.00"));
        b2.setMovimientosDebe(BigDecimal.ZERO);
        b2.setMovimientosHaber(new BigDecimal("500.00"));
        b2.setSaldoFinalDebe(BigDecimal.ZERO);
        b2.setSaldoFinalHaber(new BigDecimal("1500.00"));
        balanzas.add(b2);
        
        return balanzas;
    }
    
    private List<BalanzaComprobacion> createUnbalancedBalanzas() {
        List<BalanzaComprobacion> balanzas = new ArrayList<>();
        
        // Account 1: Debit balance
        BalanzaComprobacion b1 = new BalanzaComprobacion(UUID.randomUUID());
        b1.setSaldoInicialDebe(new BigDecimal("1000.00"));
        b1.setSaldoInicialHaber(BigDecimal.ZERO);
        b1.setMovimientosDebe(new BigDecimal("500.00"));
        b1.setMovimientosHaber(BigDecimal.ZERO);
        b1.setSaldoFinalDebe(new BigDecimal("1500.00"));
        b1.setSaldoFinalHaber(BigDecimal.ZERO);
        balanzas.add(b1);
        
        // Account 2: Credit balance (different amounts - NOT balanced!)
        BalanzaComprobacion b2 = new BalanzaComprobacion(UUID.randomUUID());
        b2.setSaldoInicialDebe(BigDecimal.ZERO);
        b2.setSaldoInicialHaber(new BigDecimal("800.00")); // Different!
        b2.setMovimientosDebe(BigDecimal.ZERO);
        b2.setMovimientosHaber(new BigDecimal("500.00"));
        b2.setSaldoFinalDebe(BigDecimal.ZERO);
        b2.setSaldoFinalHaber(new BigDecimal("1300.00"));
        balanzas.add(b2);
        
        return balanzas;
    }
}
