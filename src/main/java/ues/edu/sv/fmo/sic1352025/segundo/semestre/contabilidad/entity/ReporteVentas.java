package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * Entity representing a daily sales report (Reporte Diario de Ventas).
 */
@Entity
@Table(name = "reporte_ventas")
@NamedQueries({
    @NamedQuery(name = "ReporteVentas.findAll", query = "SELECT r FROM ReporteVentas r ORDER BY r.fecha DESC"),
    @NamedQuery(name = "ReporteVentas.findByFecha", query = "SELECT r FROM ReporteVentas r WHERE r.fecha = :fecha"),
    @NamedQuery(name = "ReporteVentas.findByRangoFechas", query = "SELECT r FROM ReporteVentas r WHERE r.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY r.fecha"),
    @NamedQuery(name = "ReporteVentas.sumTotalByMes", query = "SELECT COALESCE(SUM(r.totalVentas), 0) FROM ReporteVentas r WHERE EXTRACT(MONTH FROM r.fecha) = :mes AND EXTRACT(YEAR FROM r.fecha) = :anio")
})
public class ReporteVentas implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "id_reporte_ventas")
    private UUID idReporteVentas;
    
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    
    @Column(name = "numero_facturas")
    private Integer numeroFacturas;
    
    @Column(name = "total_ventas_gravadas")
    private BigDecimal totalVentasGravadas;
    
    @Column(name = "total_ventas_exentas")
    private BigDecimal totalVentasExentas;
    
    @Column(name = "total_iva")
    private BigDecimal totalIva;
    
    @Column(name = "total_ventas")
    private BigDecimal totalVentas;
    
    @Column(name = "primera_factura")
    private String primeraFactura;
    
    @Column(name = "ultima_factura")
    private String ultimaFactura;
    
    @Column(name = "facturas_anuladas")
    private Integer facturasAnuladas;
    
    @Column(name = "fecha_generacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaGeneracion;

    public ReporteVentas() {
    }

    public ReporteVentas(UUID idReporteVentas) {
        this.idReporteVentas = idReporteVentas;
    }

    public UUID getIdReporteVentas() {
        return idReporteVentas;
    }

    public void setIdReporteVentas(UUID idReporteVentas) {
        this.idReporteVentas = idReporteVentas;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Integer getNumeroFacturas() {
        return numeroFacturas;
    }

    public void setNumeroFacturas(Integer numeroFacturas) {
        this.numeroFacturas = numeroFacturas;
    }

    public BigDecimal getTotalVentasGravadas() {
        return totalVentasGravadas;
    }

    public void setTotalVentasGravadas(BigDecimal totalVentasGravadas) {
        this.totalVentasGravadas = totalVentasGravadas;
    }

    public BigDecimal getTotalVentasExentas() {
        return totalVentasExentas;
    }

    public void setTotalVentasExentas(BigDecimal totalVentasExentas) {
        this.totalVentasExentas = totalVentasExentas;
    }

    public BigDecimal getTotalIva() {
        return totalIva;
    }

    public void setTotalIva(BigDecimal totalIva) {
        this.totalIva = totalIva;
    }

    public BigDecimal getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(BigDecimal totalVentas) {
        this.totalVentas = totalVentas;
    }

    public String getPrimeraFactura() {
        return primeraFactura;
    }

    public void setPrimeraFactura(String primeraFactura) {
        this.primeraFactura = primeraFactura;
    }

    public String getUltimaFactura() {
        return ultimaFactura;
    }

    public void setUltimaFactura(String ultimaFactura) {
        this.ultimaFactura = ultimaFactura;
    }

    public Integer getFacturasAnuladas() {
        return facturasAnuladas;
    }

    public void setFacturasAnuladas(Integer facturasAnuladas) {
        this.facturasAnuladas = facturasAnuladas;
    }

    public Date getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(Date fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idReporteVentas != null ? idReporteVentas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ReporteVentas)) {
            return false;
        }
        ReporteVentas other = (ReporteVentas) object;
        return !((this.idReporteVentas == null && other.idReporteVentas != null) 
                || (this.idReporteVentas != null && !this.idReporteVentas.equals(other.idReporteVentas)));
    }

    @Override
    public String toString() {
        return "ReporteVentas[ idReporteVentas=" + idReporteVentas + " ]";
    }
}
