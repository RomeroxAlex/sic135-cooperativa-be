package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.CuentaContableBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.DetallePartidaDiarioBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.PartidaDiarioBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.PeriodoContableBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.PartidaDiarioDTO;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.PartidaDiarioDTO.DetallePartidaDTO;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PartidaDiario;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PeriodoContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums.EstadoPartida;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ContabilidadException;

/**
 * Unit tests for PartidaDiarioService.
 * Tests accounting validations: debe = haber, period control, etc.
 */
@ExtendWith(MockitoExtension.class)
class PartidaDiarioServiceTest {

    @Mock
    private PartidaDiarioBean partidaDiarioBean;
    
    @Mock
    private DetallePartidaDiarioBean detallePartidaDiarioBean;
    
    @Mock
    private PeriodoContableBean periodoContableBean;
    
    @Mock
    private CuentaContableBean cuentaContableBean;
    
    @InjectMocks
    private PartidaDiarioService partidaDiarioService;
    
    private PeriodoContable periodoActivo;
    private CuentaContable cuentaCaja;
    private CuentaContable cuentaIngresos;
    
    @BeforeEach
    void setUp() {
        // Set up active period
        periodoActivo = new PeriodoContable(UUID.randomUUID());
        periodoActivo.setNombre("Enero 2025");
        periodoActivo.setActivo(true);
        periodoActivo.setCerrado(false);
        periodoActivo.setFechaInicio(new Date());
        periodoActivo.setFechaFin(new Date());
        
        // Set up accounts
        cuentaCaja = new CuentaContable();
        cuentaCaja.setIdCuentaContable(UUID.randomUUID());
        cuentaCaja.setNombre("Caja");
        cuentaCaja.setNaturaleza("DEUDORA");
        
        cuentaIngresos = new CuentaContable();
        cuentaIngresos.setIdCuentaContable(UUID.randomUUID());
        cuentaIngresos.setNombre("Ingresos por Servicios");
        cuentaIngresos.setNaturaleza("ACREEDORA");
    }
    
    @Test
    @DisplayName("Should create balanced journal entry successfully")
    void crearPartidaDiario_Balanceada_Success() throws ContabilidadException {
        // Arrange
        PartidaDiarioDTO dto = createBalancedDTO(new BigDecimal("1000.00"));
        
        when(periodoContableBean.findPeriodoActivo()).thenReturn(periodoActivo);
        when(cuentaContableBean.findById(any(UUID.class)))
            .thenReturn(cuentaCaja)
            .thenReturn(cuentaIngresos);
        when(partidaDiarioBean.findMaxNumeroByPeriodo(any())).thenReturn(0);
        
        // Act
        UUID result = partidaDiarioService.crearPartidaDiario(dto);
        
        // Assert
        assertNotNull(result);
        verify(partidaDiarioBean).persistEntity(any(PartidaDiario.class));
        verify(detallePartidaDiarioBean, times(2)).persistEntity(any());
    }
    
    @Test
    @DisplayName("Should throw exception when journal entry is not balanced (debe != haber)")
    void crearPartidaDiario_NoBalanceada_ThrowsException() {
        // Arrange
        PartidaDiarioDTO dto = createUnbalancedDTO();
        
        when(periodoContableBean.findPeriodoActivo()).thenReturn(periodoActivo);
        
        // Act & Assert
        ContabilidadException exception = assertThrows(
            ContabilidadException.class,
            () -> partidaDiarioService.crearPartidaDiario(dto)
        );
        
        assertTrue(exception.getMessage().contains("no está balanceada"));
    }
    
    @Test
    @DisplayName("Should throw exception when no active period exists")
    void crearPartidaDiario_SinPeriodoActivo_ThrowsException() {
        // Arrange
        PartidaDiarioDTO dto = createBalancedDTO(new BigDecimal("1000.00"));
        when(periodoContableBean.findPeriodoActivo()).thenReturn(null);
        
        // Act & Assert
        ContabilidadException exception = assertThrows(
            ContabilidadException.class,
            () -> partidaDiarioService.crearPartidaDiario(dto)
        );
        
        assertEquals("No hay un período contable activo", exception.getMessage());
    }
    
    @Test
    @DisplayName("Should throw exception when period is closed")
    void crearPartidaDiario_PeriodoCerrado_ThrowsException() {
        // Arrange
        periodoActivo.setCerrado(true);
        PartidaDiarioDTO dto = createBalancedDTO(new BigDecimal("1000.00"));
        when(periodoContableBean.findPeriodoActivo()).thenReturn(periodoActivo);
        
        // Act & Assert
        ContabilidadException exception = assertThrows(
            ContabilidadException.class,
            () -> partidaDiarioService.crearPartidaDiario(dto)
        );
        
        assertEquals("El período contable está cerrado", exception.getMessage());
    }
    
    @Test
    @DisplayName("Should throw exception when journal entry has no details")
    void crearPartidaDiario_SinDetalles_ThrowsException() {
        // Arrange
        PartidaDiarioDTO dto = new PartidaDiarioDTO();
        dto.setFecha(new Date());
        dto.setDescripcion("Test");
        dto.setDetalles(new ArrayList<>());
        
        when(periodoContableBean.findPeriodoActivo()).thenReturn(periodoActivo);
        
        // Act & Assert
        ContabilidadException exception = assertThrows(
            ContabilidadException.class,
            () -> partidaDiarioService.crearPartidaDiario(dto)
        );
        
        assertEquals("La partida debe tener al menos un detalle", exception.getMessage());
    }
    
    @Test
    @DisplayName("Should post journal entry successfully")
    void contabilizarPartida_Success() throws ContabilidadException {
        // Arrange
        UUID idPartida = UUID.randomUUID();
        PartidaDiario partida = new PartidaDiario(idPartida);
        partida.setEstado(EstadoPartida.BORRADOR.name());
        partida.setTotalDebe(new BigDecimal("1000.00"));
        partida.setTotalHaber(new BigDecimal("1000.00"));
        
        when(partidaDiarioBean.findById(idPartida)).thenReturn(partida);
        
        // Act
        partidaDiarioService.contabilizarPartida(idPartida);
        
        // Assert
        assertEquals(EstadoPartida.CONTABILIZADA.name(), partida.getEstado());
        assertNotNull(partida.getFechaContabilizacion());
        verify(partidaDiarioBean).updateEntity(partida);
    }
    
    @Test
    @DisplayName("Should throw exception when posting non-draft entry")
    void contabilizarPartida_NoEsBorrador_ThrowsException() {
        // Arrange
        UUID idPartida = UUID.randomUUID();
        PartidaDiario partida = new PartidaDiario(idPartida);
        partida.setEstado(EstadoPartida.CONTABILIZADA.name());
        
        when(partidaDiarioBean.findById(idPartida)).thenReturn(partida);
        
        // Act & Assert
        ContabilidadException exception = assertThrows(
            ContabilidadException.class,
            () -> partidaDiarioService.contabilizarPartida(idPartida)
        );
        
        assertTrue(exception.getMessage().contains("BORRADOR"));
    }
    
    @Test
    @DisplayName("Should void journal entry successfully")
    void anularPartida_Success() throws ContabilidadException {
        // Arrange
        UUID idPartida = UUID.randomUUID();
        PartidaDiario partida = new PartidaDiario(idPartida);
        partida.setEstado(EstadoPartida.CONTABILIZADA.name());
        
        when(partidaDiarioBean.findById(idPartida)).thenReturn(partida);
        
        // Act
        partidaDiarioService.anularPartida(idPartida);
        
        // Assert
        assertEquals(EstadoPartida.ANULADA.name(), partida.getEstado());
        verify(partidaDiarioBean).updateEntity(partida);
    }
    
    // Helper methods
    private PartidaDiarioDTO createBalancedDTO(BigDecimal monto) {
        PartidaDiarioDTO dto = new PartidaDiarioDTO();
        dto.setFecha(new Date());
        dto.setDescripcion("Partida de prueba balanceada");
        dto.setConcepto("Test");
        
        List<DetallePartidaDTO> detalles = new ArrayList<>();
        
        DetallePartidaDTO detalleDebe = new DetallePartidaDTO();
        detalleDebe.setIdCuentaContable(cuentaCaja.getIdCuentaContable());
        detalleDebe.setDebe(monto);
        detalleDebe.setHaber(null);
        detalleDebe.setDescripcion("Cargo a caja");
        detalles.add(detalleDebe);
        
        DetallePartidaDTO detalleHaber = new DetallePartidaDTO();
        detalleHaber.setIdCuentaContable(cuentaIngresos.getIdCuentaContable());
        detalleHaber.setDebe(null);
        detalleHaber.setHaber(monto);
        detalleHaber.setDescripcion("Abono a ingresos");
        detalles.add(detalleHaber);
        
        dto.setDetalles(detalles);
        return dto;
    }
    
    private PartidaDiarioDTO createUnbalancedDTO() {
        PartidaDiarioDTO dto = new PartidaDiarioDTO();
        dto.setFecha(new Date());
        dto.setDescripcion("Partida de prueba NO balanceada");
        
        List<DetallePartidaDTO> detalles = new ArrayList<>();
        
        DetallePartidaDTO detalleDebe = new DetallePartidaDTO();
        detalleDebe.setIdCuentaContable(UUID.randomUUID());
        detalleDebe.setDebe(new BigDecimal("1000.00"));
        detalleDebe.setHaber(null);
        detalles.add(detalleDebe);
        
        DetallePartidaDTO detalleHaber = new DetallePartidaDTO();
        detalleHaber.setIdCuentaContable(UUID.randomUUID());
        detalleHaber.setDebe(null);
        detalleHaber.setHaber(new BigDecimal("500.00")); // Different amount - not balanced!
        detalles.add(detalleHaber);
        
        dto.setDetalles(detalles);
        return dto;
    }
}
