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
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.PartidaAjusteBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.PartidaDiarioBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.PeriodoContableBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.PartidaAjusteDTO;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PartidaAjuste;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PartidaDiario;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PeriodoContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums.EstadoAjuste;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums.TipoAjuste;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ContabilidadException;

/**
 * Unit tests for PartidaAjusteService.
 * Tests adjustment entry creation, application, and impact on ledger.
 */
@ExtendWith(MockitoExtension.class)
class PartidaAjusteServiceTest {

    @Mock
    private PartidaAjusteBean partidaAjusteBean;
    
    @Mock
    private PartidaDiarioBean partidaDiarioBean;
    
    @Mock
    private DetallePartidaDiarioBean detallePartidaDiarioBean;
    
    @Mock
    private PeriodoContableBean periodoContableBean;
    
    @Mock
    private CuentaContableBean cuentaContableBean;
    
    @InjectMocks
    private PartidaAjusteService partidaAjusteService;
    
    private PeriodoContable periodoActivo;
    private CuentaContable cuentaGastoDepreciacion;
    private CuentaContable cuentaDepreciacionAcumulada;
    
    @BeforeEach
    void setUp() {
        // Set up active period
        periodoActivo = new PeriodoContable(UUID.randomUUID());
        periodoActivo.setNombre("Enero 2025");
        periodoActivo.setActivo(true);
        periodoActivo.setCerrado(false);
        
        // Set up accounts for depreciation
        cuentaGastoDepreciacion = new CuentaContable();
        cuentaGastoDepreciacion.setIdCuentaContable(UUID.randomUUID());
        cuentaGastoDepreciacion.setNombre("Gasto Depreciación");
        cuentaGastoDepreciacion.setNaturaleza("DEUDORA");
        
        cuentaDepreciacionAcumulada = new CuentaContable();
        cuentaDepreciacionAcumulada.setIdCuentaContable(UUID.randomUUID());
        cuentaDepreciacionAcumulada.setNombre("Depreciación Acumulada");
        cuentaDepreciacionAcumulada.setNaturaleza("ACREEDORA");
    }
    
    @Test
    @DisplayName("Should create adjustment entry successfully")
    void crearPartidaAjuste_Success() throws ContabilidadException {
        // Arrange
        PartidaAjusteDTO dto = createAjusteDTO(
            TipoAjuste.DEPRECIACION.name(),
            new BigDecimal("500.00")
        );
        
        when(periodoContableBean.findPeriodoActivo()).thenReturn(periodoActivo);
        when(cuentaContableBean.findById(dto.getIdCuentaDebe())).thenReturn(cuentaGastoDepreciacion);
        when(cuentaContableBean.findById(dto.getIdCuentaHaber())).thenReturn(cuentaDepreciacionAcumulada);
        when(partidaAjusteBean.findByPeriodo(any())).thenReturn(new ArrayList<>());
        
        // Act
        UUID result = partidaAjusteService.crearPartidaAjuste(dto);
        
        // Assert
        assertNotNull(result);
        verify(partidaAjusteBean).persistEntity(any(PartidaAjuste.class));
    }
    
    @Test
    @DisplayName("Should throw exception when adjustment amount is zero or negative")
    void crearPartidaAjuste_MontoInvalido_ThrowsException() {
        // Arrange
        PartidaAjusteDTO dto = createAjusteDTO(
            TipoAjuste.DEPRECIACION.name(),
            BigDecimal.ZERO
        );
        
        // Act & Assert
        ContabilidadException exception = assertThrows(
            ContabilidadException.class,
            () -> partidaAjusteService.crearPartidaAjuste(dto)
        );
        
        assertTrue(exception.getMessage().contains("monto"));
    }
    
    @Test
    @DisplayName("Should throw exception when debit account is missing")
    void crearPartidaAjuste_SinCuentaDebe_ThrowsException() {
        // Arrange
        PartidaAjusteDTO dto = new PartidaAjusteDTO();
        dto.setMonto(new BigDecimal("500.00"));
        dto.setIdCuentaDebe(null);
        dto.setIdCuentaHaber(UUID.randomUUID());
        
        // Act & Assert
        ContabilidadException exception = assertThrows(
            ContabilidadException.class,
            () -> partidaAjusteService.crearPartidaAjuste(dto)
        );
        
        assertTrue(exception.getMessage().contains("debe"));
    }
    
    @Test
    @DisplayName("Should apply adjustment and generate journal entry")
    void aplicarAjuste_Success() throws ContabilidadException {
        // Arrange
        UUID idAjuste = UUID.randomUUID();
        PartidaAjuste ajuste = createPendingAjuste(idAjuste);
        
        when(partidaAjusteBean.findById(idAjuste)).thenReturn(ajuste);
        when(partidaDiarioBean.findMaxNumeroByPeriodo(any())).thenReturn(0);
        when(partidaDiarioBean.findById(any())).thenReturn(new PartidaDiario(UUID.randomUUID()));
        
        // Act
        UUID idPartida = partidaAjusteService.aplicarAjuste(idAjuste);
        
        // Assert
        assertNotNull(idPartida);
        assertEquals(EstadoAjuste.APLICADO.name(), ajuste.getEstado());
        assertNotNull(ajuste.getFechaAplicacion());
        verify(partidaDiarioBean).persistEntity(any(PartidaDiario.class));
        verify(detallePartidaDiarioBean, times(2)).persistEntity(any());
    }
    
    @Test
    @DisplayName("Should throw exception when applying non-pending adjustment")
    void aplicarAjuste_NoEstaPendiente_ThrowsException() {
        // Arrange
        UUID idAjuste = UUID.randomUUID();
        PartidaAjuste ajuste = createPendingAjuste(idAjuste);
        ajuste.setEstado(EstadoAjuste.APLICADO.name());
        
        when(partidaAjusteBean.findById(idAjuste)).thenReturn(ajuste);
        
        // Act & Assert
        ContabilidadException exception = assertThrows(
            ContabilidadException.class,
            () -> partidaAjusteService.aplicarAjuste(idAjuste)
        );
        
        assertTrue(exception.getMessage().contains("PENDIENTE"));
    }
    
    @Test
    @DisplayName("Should void adjustment entry successfully")
    void anularAjuste_Success() throws ContabilidadException {
        // Arrange
        UUID idAjuste = UUID.randomUUID();
        PartidaAjuste ajuste = createPendingAjuste(idAjuste);
        
        when(partidaAjusteBean.findById(idAjuste)).thenReturn(ajuste);
        
        // Act
        partidaAjusteService.anularAjuste(idAjuste);
        
        // Assert
        assertEquals(EstadoAjuste.ANULADO.name(), ajuste.getEstado());
        verify(partidaAjusteBean).updateEntity(ajuste);
    }
    
    @Test
    @DisplayName("Should generate automatic depreciation adjustment")
    void generarDepreciacionAutomatica_Success() throws ContabilidadException {
        // Arrange
        UUID idPeriodo = periodoActivo.getIdPeriodoContable();
        BigDecimal monto = new BigDecimal("500.00");
        
        when(periodoContableBean.findById(idPeriodo)).thenReturn(periodoActivo);
        when(cuentaContableBean.findById(any())).thenReturn(cuentaGastoDepreciacion, cuentaDepreciacionAcumulada);
        when(partidaAjusteBean.findByPeriodo(any())).thenReturn(new ArrayList<>());
        
        // Act
        UUID result = partidaAjusteService.generarDepreciacionAutomatica(
            idPeriodo,
            cuentaGastoDepreciacion.getIdCuentaContable(),
            cuentaDepreciacionAcumulada.getIdCuentaContable(),
            monto,
            "Depreciación mensual equipo"
        );
        
        // Assert
        assertNotNull(result);
        verify(partidaAjusteBean).persistEntity(argThat(ajuste -> 
            ajuste.getTipoAjuste().equals(TipoAjuste.DEPRECIACION.name()) &&
            ajuste.getAutomatico()
        ));
    }
    
    @Test
    @DisplayName("Should generate automatic provision adjustment")
    void generarProvisionAutomatica_Success() throws ContabilidadException {
        // Arrange
        UUID idPeriodo = periodoActivo.getIdPeriodoContable();
        BigDecimal monto = new BigDecimal("1000.00");
        
        when(periodoContableBean.findById(idPeriodo)).thenReturn(periodoActivo);
        when(cuentaContableBean.findById(any())).thenReturn(cuentaGastoDepreciacion, cuentaDepreciacionAcumulada);
        when(partidaAjusteBean.findByPeriodo(any())).thenReturn(new ArrayList<>());
        
        // Act
        UUID result = partidaAjusteService.generarProvisionAutomatica(
            idPeriodo,
            cuentaGastoDepreciacion.getIdCuentaContable(),
            cuentaDepreciacionAcumulada.getIdCuentaContable(),
            monto,
            "Provisión cuentas incobrables"
        );
        
        // Assert
        assertNotNull(result);
        verify(partidaAjusteBean).persistEntity(argThat(ajuste -> 
            ajuste.getTipoAjuste().equals(TipoAjuste.PROVISION.name()) &&
            ajuste.getAutomatico()
        ));
    }
    
    // Helper methods
    private PartidaAjusteDTO createAjusteDTO(String tipo, BigDecimal monto) {
        PartidaAjusteDTO dto = new PartidaAjusteDTO();
        dto.setFecha(new Date());
        dto.setDescripcion("Ajuste de prueba");
        dto.setConcepto("Test");
        dto.setTipoAjuste(tipo);
        dto.setAutomatico(false);
        dto.setMonto(monto);
        dto.setIdCuentaDebe(cuentaGastoDepreciacion.getIdCuentaContable());
        dto.setIdCuentaHaber(cuentaDepreciacionAcumulada.getIdCuentaContable());
        return dto;
    }
    
    private PartidaAjuste createPendingAjuste(UUID id) {
        PartidaAjuste ajuste = new PartidaAjuste(id);
        ajuste.setFecha(new Date());
        ajuste.setDescripcion("Ajuste pendiente");
        ajuste.setTipoAjuste(TipoAjuste.DEPRECIACION.name());
        ajuste.setEstado(EstadoAjuste.PENDIENTE.name());
        ajuste.setMonto(new BigDecimal("500.00"));
        ajuste.setIdPeriodoContable(periodoActivo);
        ajuste.setIdCuentaDebe(cuentaGastoDepreciacion);
        ajuste.setIdCuentaHaber(cuentaDepreciacionAcumulada);
        return ajuste;
    }
}
