package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.DetalleFacturaBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.FacturaBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.ReporteVentasBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control.SocioBean;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.FacturaDTO;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto.FacturaDTO.DetalleFacturaDTO;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.DetalleFactura;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.Factura;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.ReporteVentas;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.Socio;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception.ContabilidadException;

/**
 * Service for managing digital invoices (Facturas) and daily sales reports.
 */
@Stateless
public class FacturaService {

    @Inject
    FacturaBean facturaBean;
    
    @Inject
    DetalleFacturaBean detalleFacturaBean;
    
    @Inject
    SocioBean socioBean;
    
    @Inject
    ReporteVentasBean reporteVentasBean;

    /**
     * Create a new digital invoice.
     * @param dto invoice data
     * @return created invoice ID
     * @throws ContabilidadException if validation fails
     */
    public UUID crearFactura(FacturaDTO dto) throws ContabilidadException {
        if (dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
            throw new ContabilidadException("La factura debe tener al menos un detalle");
        }
        
        // Validate client if provided
        Socio socio = null;
        if (dto.getIdSocio() != null) {
            socio = socioBean.findById(dto.getIdSocio());
        }
        
        // Generate invoice number
        String numeroFactura = generarNumeroFactura(dto.getSerie());
        
        // Create invoice
        UUID idFactura = UUID.randomUUID();
        Factura factura = new Factura(idFactura);
        factura.setNumeroFactura(numeroFactura);
        factura.setSerie(dto.getSerie() != null ? dto.getSerie() : "A");
        factura.setFecha(dto.getFecha() != null ? dto.getFecha() : new Date());
        factura.setFechaEmision(new Date());
        factura.setEstado("BORRADOR");
        factura.setTipoFactura(dto.getTipoFactura() != null ? dto.getTipoFactura() : "CONSUMIDOR_FINAL");
        factura.setNombreCliente(dto.getNombreCliente());
        factura.setNitCliente(dto.getNitCliente());
        factura.setDireccionCliente(dto.getDireccionCliente());
        factura.setObservaciones(dto.getObservaciones());
        factura.setIdSocio(socio);
        
        // Create detail lines and calculate totals
        BigDecimal subtotal = BigDecimal.ZERO;
        List<DetalleFactura> detalles = new ArrayList<>();
        int numeroLinea = 1;
        
        for (DetalleFacturaDTO detalleDto : dto.getDetalles()) {
            DetalleFactura detalle = new DetalleFactura(UUID.randomUUID());
            detalle.setNumeroLinea(numeroLinea++);
            detalle.setDescripcion(detalleDto.getDescripcion());
            detalle.setCantidad(detalleDto.getCantidad() != null ? detalleDto.getCantidad() : BigDecimal.ONE);
            detalle.setPrecioUnitario(detalleDto.getPrecioUnitario() != null ? detalleDto.getPrecioUnitario() : BigDecimal.ZERO);
            detalle.calcularSubtotal();
            detalle.setIdFactura(factura);
            detalles.add(detalle);
            
            subtotal = subtotal.add(detalle.getSubtotal());
        }
        
        // Calculate IVA (13%)
        BigDecimal iva = subtotal.multiply(new BigDecimal("0.13"));
        BigDecimal total = subtotal.add(iva);
        
        factura.setSubtotal(subtotal);
        factura.setIva(iva);
        factura.setTotal(total);
        factura.setDetalles(detalles);
        
        facturaBean.persistEntity(factura);
        
        for (DetalleFactura detalle : detalles) {
            detalleFacturaBean.persistEntity(detalle);
        }
        
        return idFactura;
    }

    /**
     * Emit an invoice (change status from BORRADOR to EMITIDA).
     * @param idFactura invoice ID
     * @throws ContabilidadException if validation fails
     */
    public void emitirFactura(UUID idFactura) throws ContabilidadException {
        Factura factura = facturaBean.findById(idFactura);
        
        if (factura == null) {
            throw new ContabilidadException("Factura no encontrada");
        }
        
        if (!"BORRADOR".equals(factura.getEstado())) {
            throw new ContabilidadException("Solo se pueden emitir facturas en estado BORRADOR");
        }
        
        factura.setEstado("EMITIDA");
        factura.setFechaEmision(new Date());
        facturaBean.updateEntity(factura);
    }

    /**
     * Void an invoice.
     * @param idFactura invoice ID
     * @throws ContabilidadException if validation fails
     */
    public void anularFactura(UUID idFactura) throws ContabilidadException {
        Factura factura = facturaBean.findById(idFactura);
        
        if (factura == null) {
            throw new ContabilidadException("Factura no encontrada");
        }
        
        if ("ANULADA".equals(factura.getEstado())) {
            throw new ContabilidadException("La factura ya está anulada");
        }
        
        factura.setEstado("ANULADA");
        facturaBean.updateEntity(factura);
    }

    /**
     * Generate daily sales report for a specific date.
     * @param fecha report date
     * @return generated report ID
     * @throws ContabilidadException if validation fails
     */
    public UUID generarReporteDiario(Date fecha) throws ContabilidadException {
        // Check if report already exists for this date
        ReporteVentas existente = reporteVentasBean.findByFecha(fecha);
        if (existente != null) {
            // Delete existing report to regenerate
            reporteVentasBean.deleteEntity(existente);
        }
        
        // Get all invoices for the date
        List<Factura> facturas = facturaBean.findByFecha(fecha);
        
        BigDecimal totalVentasGravadas = BigDecimal.ZERO;
        BigDecimal totalVentasExentas = BigDecimal.ZERO;
        BigDecimal totalIva = BigDecimal.ZERO;
        BigDecimal totalVentas = BigDecimal.ZERO;
        int numeroFacturas = 0;
        int facturasAnuladas = 0;
        String primeraFactura = null;
        String ultimaFactura = null;
        
        for (Factura factura : facturas) {
            if ("EMITIDA".equals(factura.getEstado())) {
                numeroFacturas++;
                totalVentasGravadas = totalVentasGravadas.add(factura.getSubtotal() != null ? factura.getSubtotal() : BigDecimal.ZERO);
                totalIva = totalIva.add(factura.getIva() != null ? factura.getIva() : BigDecimal.ZERO);
                totalVentas = totalVentas.add(factura.getTotal() != null ? factura.getTotal() : BigDecimal.ZERO);
                
                if (primeraFactura == null) {
                    primeraFactura = factura.getNumeroFactura();
                }
                ultimaFactura = factura.getNumeroFactura();
            } else if ("ANULADA".equals(factura.getEstado())) {
                facturasAnuladas++;
            }
        }
        
        // Create report
        UUID idReporte = UUID.randomUUID();
        ReporteVentas reporte = new ReporteVentas(idReporte);
        reporte.setFecha(fecha);
        reporte.setNumeroFacturas(numeroFacturas);
        reporte.setTotalVentasGravadas(totalVentasGravadas);
        reporte.setTotalVentasExentas(totalVentasExentas);
        reporte.setTotalIva(totalIva);
        reporte.setTotalVentas(totalVentas);
        reporte.setPrimeraFactura(primeraFactura);
        reporte.setUltimaFactura(ultimaFactura);
        reporte.setFacturasAnuladas(facturasAnuladas);
        reporte.setFechaGeneracion(new Date());
        
        reporteVentasBean.persistEntity(reporte);
        
        return idReporte;
    }

    /**
     * Get invoice by ID.
     * @param idFactura invoice ID
     * @return invoice or null
     */
    public Factura findById(UUID idFactura) {
        return facturaBean.findById(idFactura);
    }

    /**
     * Get all invoices.
     * @return list of invoices
     */
    public List<Factura> findAll() {
        return facturaBean.findAll();
    }

    /**
     * Get invoices by date.
     * @param fecha invoice date
     * @return list of invoices
     */
    public List<Factura> findByFecha(Date fecha) {
        return facturaBean.findByFecha(fecha);
    }

    /**
     * Get daily sales report by date.
     * @param fecha report date
     * @return sales report or null
     */
    public ReporteVentas getReporteDiario(Date fecha) {
        return reporteVentasBean.findByFecha(fecha);
    }

    /**
     * Get all daily sales reports.
     * @return list of sales reports
     */
    public List<ReporteVentas> findAllReportes() {
        return reporteVentasBean.findAll();
    }

    /**
     * Generate sequential invoice number using MAX query for reliability.
     */
    private String generarNumeroFactura(String serie) {
        List<Factura> todas = facturaBean.findAll();
        int numero = 1;
        if (todas != null && !todas.isEmpty()) {
            // Extract max number from existing invoices to avoid duplicates
            numero = todas.stream()
                .map(Factura::getNumeroFactura)
                .filter(n -> n != null)
                .map(n -> {
                    try {
                        // Extract numeric part after the hyphen
                        String[] parts = n.split("-");
                        if (parts.length > 1) {
                            return Integer.parseInt(parts[parts.length - 1]);
                        }
                        return 0;
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .max(Integer::compareTo)
                .orElse(0) + 1;
        }
        return String.format("%s-%08d", serie != null ? serie : "A", numero);
    }
}
