package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * DTO for creating/updating invoices (Factura).
 */
public class FacturaDTO {
    
    private UUID idFactura;
    private String serie;
    private Date fecha;
    private String tipoFactura;
    private String nombreCliente;
    private String nitCliente;
    private String direccionCliente;
    private String observaciones;
    private UUID idSocio;
    private List<DetalleFacturaDTO> detalles;
    
    public UUID getIdFactura() {
        return idFactura;
    }
    
    public void setIdFactura(UUID idFactura) {
        this.idFactura = idFactura;
    }
    
    public String getSerie() {
        return serie;
    }
    
    public void setSerie(String serie) {
        this.serie = serie;
    }
    
    public Date getFecha() {
        return fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    public String getTipoFactura() {
        return tipoFactura;
    }
    
    public void setTipoFactura(String tipoFactura) {
        this.tipoFactura = tipoFactura;
    }
    
    public String getNombreCliente() {
        return nombreCliente;
    }
    
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
    
    public String getNitCliente() {
        return nitCliente;
    }
    
    public void setNitCliente(String nitCliente) {
        this.nitCliente = nitCliente;
    }
    
    public String getDireccionCliente() {
        return direccionCliente;
    }
    
    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }
    
    public String getObservaciones() {
        return observaciones;
    }
    
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    
    public UUID getIdSocio() {
        return idSocio;
    }
    
    public void setIdSocio(UUID idSocio) {
        this.idSocio = idSocio;
    }
    
    public List<DetalleFacturaDTO> getDetalles() {
        return detalles;
    }
    
    public void setDetalles(List<DetalleFacturaDTO> detalles) {
        this.detalles = detalles;
    }
    
    /**
     * Inner class for invoice detail lines
     */
    public static class DetalleFacturaDTO {
        private String descripcion;
        private BigDecimal cantidad;
        private BigDecimal precioUnitario;
        
        public String getDescripcion() {
            return descripcion;
        }
        
        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public BigDecimal getCantidad() {
            return cantidad;
        }
        
        public void setCantidad(BigDecimal cantidad) {
            this.cantidad = cantidad;
        }
        
        public BigDecimal getPrecioUnitario() {
            return precioUnitario;
        }
        
        public void setPrecioUnitario(BigDecimal precioUnitario) {
            this.precioUnitario = precioUnitario;
        }
    }
}
