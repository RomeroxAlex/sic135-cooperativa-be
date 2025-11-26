package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for accounting entity validations.
 * Tests balance validation methods and calculations.
 */
class AccountingEntitiesTest {

    @Test
    @DisplayName("PartidaDiario should detect balanced entry")
    void partidaDiario_EstaBalanceada_ReturnsTrue() {
        // Arrange
        PartidaDiario partida = new PartidaDiario(UUID.randomUUID());
        partida.setTotalDebe(new BigDecimal("1000.00"));
        partida.setTotalHaber(new BigDecimal("1000.00"));
        
        // Act & Assert
        assertTrue(partida.estaBalanceada());
    }
    
    @Test
    @DisplayName("PartidaDiario should detect unbalanced entry")
    void partidaDiario_NoEstaBalanceada_ReturnsFalse() {
        // Arrange
        PartidaDiario partida = new PartidaDiario(UUID.randomUUID());
        partida.setTotalDebe(new BigDecimal("1000.00"));
        partida.setTotalHaber(new BigDecimal("500.00"));
        
        // Act & Assert
        assertFalse(partida.estaBalanceada());
    }
    
    @Test
    @DisplayName("PartidaDiario should handle null values")
    void partidaDiario_ValoresNulos_ReturnsFalse() {
        // Arrange
        PartidaDiario partida = new PartidaDiario(UUID.randomUUID());
        partida.setTotalDebe(null);
        partida.setTotalHaber(null);
        
        // Act & Assert
        assertFalse(partida.estaBalanceada());
    }
    
    @Test
    @DisplayName("LibroMayor should calculate final balance for debit account")
    void libroMayor_CalculaSaldoFinal_CuentaDeudora() {
        // Arrange
        LibroMayor libroMayor = new LibroMayor(UUID.randomUUID());
        libroMayor.setSaldoInicial(new BigDecimal("1000.00"));
        libroMayor.setTotalDebe(new BigDecimal("500.00"));
        libroMayor.setTotalHaber(new BigDecimal("200.00"));
        
        // Act
        libroMayor.calcularSaldoFinal(true); // Debit nature
        
        // Assert - For debit accounts: initial + debe - haber
        // 1000 + 500 - 200 = 1300
        assertEquals(new BigDecimal("1300.00"), libroMayor.getSaldoFinal());
    }
    
    @Test
    @DisplayName("LibroMayor should calculate final balance for credit account")
    void libroMayor_CalculaSaldoFinal_CuentaAcreedora() {
        // Arrange
        LibroMayor libroMayor = new LibroMayor(UUID.randomUUID());
        libroMayor.setSaldoInicial(new BigDecimal("1000.00"));
        libroMayor.setTotalDebe(new BigDecimal("200.00"));
        libroMayor.setTotalHaber(new BigDecimal("500.00"));
        
        // Act
        libroMayor.calcularSaldoFinal(false); // Credit nature
        
        // Assert - For credit accounts: initial - debe + haber
        // 1000 - 200 + 500 = 1300
        assertEquals(new BigDecimal("1300.00"), libroMayor.getSaldoFinal());
    }
    
    @Test
    @DisplayName("LibroMayor should handle null values in calculation")
    void libroMayor_CalculaSaldoFinal_ValoresNulos() {
        // Arrange
        LibroMayor libroMayor = new LibroMayor(UUID.randomUUID());
        libroMayor.setSaldoInicial(null);
        libroMayor.setTotalDebe(null);
        libroMayor.setTotalHaber(null);
        
        // Act
        libroMayor.calcularSaldoFinal(true);
        
        // Assert - All nulls should be treated as zero
        assertEquals(BigDecimal.ZERO, libroMayor.getSaldoFinal());
    }
    
    @Test
    @DisplayName("EstadoFinanciero should validate balance sheet equation")
    void estadoFinanciero_ValidaEcuacion_Balanceado() {
        // Arrange
        EstadoFinanciero estado = new EstadoFinanciero(UUID.randomUUID());
        estado.setTotalActivos(new BigDecimal("10000.00"));
        estado.setTotalPasivos(new BigDecimal("4000.00"));
        estado.setTotalPatrimonio(new BigDecimal("6000.00"));
        
        // Act & Assert - Activos = Pasivos + Patrimonio
        // 10000 = 4000 + 6000 ✓
        assertTrue(estado.balanceSheetEquationHolds());
    }
    
    @Test
    @DisplayName("EstadoFinanciero should detect invalid balance sheet equation")
    void estadoFinanciero_ValidaEcuacion_NoBalanceado() {
        // Arrange
        EstadoFinanciero estado = new EstadoFinanciero(UUID.randomUUID());
        estado.setTotalActivos(new BigDecimal("10000.00"));
        estado.setTotalPasivos(new BigDecimal("4000.00"));
        estado.setTotalPatrimonio(new BigDecimal("5000.00")); // Wrong!
        
        // Act & Assert - 10000 != 4000 + 5000
        assertFalse(estado.balanceSheetEquationHolds());
    }
    
    @Test
    @DisplayName("DetallePartidaDiario should calculate net movement")
    void detallePartida_CalculaMovimientoNeto() {
        // Arrange
        DetallePartidaDiario detalle = new DetallePartidaDiario(UUID.randomUUID());
        detalle.setDebe(new BigDecimal("1000.00"));
        detalle.setHaber(new BigDecimal("300.00"));
        
        // Act
        BigDecimal movimiento = detalle.getMovimientoNeto();
        
        // Assert - debe - haber = 1000 - 300 = 700
        assertEquals(new BigDecimal("700.00"), movimiento);
    }
    
    @Test
    @DisplayName("DetalleFactura should calculate subtotal")
    void detalleFactura_CalculaSubtotal() {
        // Arrange
        DetalleFactura detalle = new DetalleFactura(UUID.randomUUID());
        detalle.setCantidad(new BigDecimal("5"));
        detalle.setPrecioUnitario(new BigDecimal("100.00"));
        
        // Act
        detalle.calcularSubtotal();
        
        // Assert - 5 * 100 = 500
        assertEquals(new BigDecimal("500.00"), detalle.getSubtotal());
    }
    
    @Test
    @DisplayName("DetalleFactura should handle null values")
    void detalleFactura_CalculaSubtotal_ValoresNulos() {
        // Arrange
        DetalleFactura detalle = new DetalleFactura(UUID.randomUUID());
        detalle.setCantidad(null);
        detalle.setPrecioUnitario(null);
        
        // Act
        detalle.calcularSubtotal();
        
        // Assert
        assertEquals(BigDecimal.ZERO, detalle.getSubtotal());
    }
}
