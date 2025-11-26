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

import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.DetalleFacturaBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.FacturaBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.ReporteVentasBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.SocioBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.FacturaDTO;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.FacturaDTO.DetalleFacturaDTO;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.Factura;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.ReporteVentas;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ContabilidadException;

/**
 * Unit tests for FacturaService.
 * Tests invoice creation, emission, IVA calculation, and daily sales reports.
 */
@ExtendWith(MockitoExtension.class)
class FacturaServiceTest {

    @Mock
    private FacturaBean facturaBean;
    
    @Mock
    private DetalleFacturaBean detalleFacturaBean;
    
    @Mock
    private SocioBean socioBean;
    
    @Mock
    private ReporteVentasBean reporteVentasBean;
    
    @InjectMocks
    private FacturaService facturaService;
    
    @Test
    @DisplayName("Should create invoice with correct IVA calculation")
    void crearFactura_CalculaIVACorrectamente() throws ContabilidadException {
        // Arrange
        FacturaDTO dto = createFacturaDTO(new BigDecimal("100.00"));
        when(facturaBean.findAll()).thenReturn(new ArrayList<>());
        
        // Act
        UUID result = facturaService.crearFactura(dto);
        
        // Assert
        assertNotNull(result);
        verify(facturaBean).persistEntity(argThat(factura -> {
            // Verify IVA calculation: 100 * 0.13 = 13
            BigDecimal expectedIva = new BigDecimal("13.00");
            BigDecimal expectedTotal = new BigDecimal("113.00");
            
            return factura.getSubtotal().compareTo(new BigDecimal("100.00")) == 0 &&
                   factura.getIva().setScale(2).compareTo(expectedIva) == 0 &&
                   factura.getTotal().setScale(2).compareTo(expectedTotal) == 0;
        }));
    }
    
    @Test
    @DisplayName("Should throw exception when invoice has no details")
    void crearFactura_SinDetalles_ThrowsException() {
        // Arrange
        FacturaDTO dto = new FacturaDTO();
        dto.setFecha(new Date());
        dto.setDetalles(new ArrayList<>());
        
        // Act & Assert
        ContabilidadException exception = assertThrows(
            ContabilidadException.class,
            () -> facturaService.crearFactura(dto)
        );
        
        assertEquals("La factura debe tener al menos un detalle", exception.getMessage());
    }
    
    @Test
    @DisplayName("Should emit invoice successfully")
    void emitirFactura_Success() throws ContabilidadException {
        // Arrange
        UUID idFactura = UUID.randomUUID();
        Factura factura = new Factura(idFactura);
        factura.setEstado("BORRADOR");
        
        when(facturaBean.findById(idFactura)).thenReturn(factura);
        
        // Act
        facturaService.emitirFactura(idFactura);
        
        // Assert
        assertEquals("EMITIDA", factura.getEstado());
        assertNotNull(factura.getFechaEmision());
        verify(facturaBean).updateEntity(factura);
    }
    
    @Test
    @DisplayName("Should throw exception when emitting non-draft invoice")
    void emitirFactura_NoEsBorrador_ThrowsException() {
        // Arrange
        UUID idFactura = UUID.randomUUID();
        Factura factura = new Factura(idFactura);
        factura.setEstado("EMITIDA");
        
        when(facturaBean.findById(idFactura)).thenReturn(factura);
        
        // Act & Assert
        ContabilidadException exception = assertThrows(
            ContabilidadException.class,
            () -> facturaService.emitirFactura(idFactura)
        );
        
        assertTrue(exception.getMessage().contains("BORRADOR"));
    }
    
    @Test
    @DisplayName("Should void invoice successfully")
    void anularFactura_Success() throws ContabilidadException {
        // Arrange
        UUID idFactura = UUID.randomUUID();
        Factura factura = new Factura(idFactura);
        factura.setEstado("EMITIDA");
        
        when(facturaBean.findById(idFactura)).thenReturn(factura);
        
        // Act
        facturaService.anularFactura(idFactura);
        
        // Assert
        assertEquals("ANULADA", factura.getEstado());
        verify(facturaBean).updateEntity(factura);
    }
    
    @Test
    @DisplayName("Should generate daily sales report correctly")
    void generarReporteDiario_Success() throws ContabilidadException {
        // Arrange
        Date fecha = new Date();
        List<Factura> facturas = createFacturasEmitidas(3);
        
        when(reporteVentasBean.findByFecha(fecha)).thenReturn(null);
        when(facturaBean.findByFecha(fecha)).thenReturn(facturas);
        
        // Act
        UUID result = facturaService.generarReporteDiario(fecha);
        
        // Assert
        assertNotNull(result);
        verify(reporteVentasBean).persistEntity(argThat(reporte -> {
            // Verify report totals
            return reporte.getNumeroFacturas() == 3 &&
                   reporte.getTotalVentas().compareTo(BigDecimal.ZERO) > 0;
        }));
    }
    
    @Test
    @DisplayName("Should count voided invoices in daily report")
    void generarReporteDiario_CuentaAnuladas() throws ContabilidadException {
        // Arrange
        Date fecha = new Date();
        List<Factura> facturas = new ArrayList<>();
        facturas.addAll(createFacturasEmitidas(2));
        
        // Add voided invoice
        Factura anulada = new Factura(UUID.randomUUID());
        anulada.setEstado("ANULADA");
        anulada.setNumeroFactura("A-00000003");
        facturas.add(anulada);
        
        when(reporteVentasBean.findByFecha(fecha)).thenReturn(null);
        when(facturaBean.findByFecha(fecha)).thenReturn(facturas);
        
        // Act
        UUID result = facturaService.generarReporteDiario(fecha);
        
        // Assert
        assertNotNull(result);
        verify(reporteVentasBean).persistEntity(argThat(reporte -> 
            reporte.getNumeroFacturas() == 2 && // Only emitted
            reporte.getFacturasAnuladas() == 1
        ));
    }
    
    // Helper methods
    private FacturaDTO createFacturaDTO(BigDecimal precioUnitario) {
        FacturaDTO dto = new FacturaDTO();
        dto.setFecha(new Date());
        dto.setNombreCliente("Cliente de Prueba");
        dto.setTipoFactura("CONSUMIDOR_FINAL");
        
        List<DetalleFacturaDTO> detalles = new ArrayList<>();
        DetalleFacturaDTO detalle = new DetalleFacturaDTO();
        detalle.setDescripcion("Servicio de prueba");
        detalle.setCantidad(BigDecimal.ONE);
        detalle.setPrecioUnitario(precioUnitario);
        detalles.add(detalle);
        
        dto.setDetalles(detalles);
        return dto;
    }
    
    private List<Factura> createFacturasEmitidas(int cantidad) {
        List<Factura> facturas = new ArrayList<>();
        for (int i = 1; i <= cantidad; i++) {
            Factura factura = new Factura(UUID.randomUUID());
            factura.setEstado("EMITIDA");
            factura.setNumeroFactura("A-" + String.format("%08d", i));
            factura.setSubtotal(new BigDecimal("100.00"));
            factura.setIva(new BigDecimal("13.00"));
            factura.setTotal(new BigDecimal("113.00"));
            facturas.add(factura);
        }
        return facturas;
    }
}
