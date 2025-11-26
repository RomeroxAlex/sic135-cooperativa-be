package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 * Entity representing a digital invoice (Factura Digital).
 */
@Entity
@Table(name = "factura")
@NamedQueries({
    @NamedQuery(name = "Factura.findAll", query = "SELECT f FROM Factura f ORDER BY f.fecha DESC"),
    @NamedQuery(name = "Factura.findByNumero", query = "SELECT f FROM Factura f WHERE f.numeroFactura = :numero"),
    @NamedQuery(name = "Factura.findByFecha", query = "SELECT f FROM Factura f WHERE f.fecha = :fecha"),
    @NamedQuery(name = "Factura.findByRangoFechas", query = "SELECT f FROM Factura f WHERE f.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY f.fecha"),
    @NamedQuery(name = "Factura.findBySocio", query = "SELECT f FROM Factura f WHERE f.idSocio = :socio"),
    @NamedQuery(name = "Factura.findByEstado", query = "SELECT f FROM Factura f WHERE f.estado = :estado"),
    @NamedQuery(name = "Factura.sumVentasByFecha", query = "SELECT COALESCE(SUM(f.total), 0) FROM Factura f WHERE f.fecha = :fecha AND f.estado = 'EMITIDA'")
})
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Basic(optional = false)
    @Column(name = "id_factura")
    private UUID idFactura;
    
    @Column(name = "numero_factura")
    private String numeroFactura;
    
    @Column(name = "serie")
    private String serie;
    
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    
    @Column(name = "fecha_emision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;
    
    @Column(name = "subtotal")
    private BigDecimal subtotal;
    
    @Column(name = "iva")
    private BigDecimal iva;
    
    @Column(name = "total")
    private BigDecimal total;
    
    @Column(name = "estado")
    private String estado; // BORRADOR, EMITIDA, ANULADA
    
    @Column(name = "tipo_factura")
    private String tipoFactura; // CONSUMIDOR_FINAL, CREDITO_FISCAL
    
    @Column(name = "nombre_cliente")
    private String nombreCliente;
    
    @Column(name = "nit_cliente")
    private String nitCliente;
    
    @Column(name = "direccion_cliente")
    private String direccionCliente;
    
    @Column(name = "observaciones")
    private String observaciones;
    
    @JoinColumn(name = "id_socio", referencedColumnName = "id_socio")
    @ManyToOne(fetch = FetchType.LAZY)
    private Socio idSocio;
    
    @OneToMany(mappedBy = "idFactura", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetalleFactura> detalles;

    public Factura() {
    }

    public Factura(UUID idFactura) {
        this.idFactura = idFactura;
    }

    public UUID getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(UUID idFactura) {
        this.idFactura = idFactura;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
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

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getIva() {
        return iva;
    }

    public void setIva(BigDecimal iva) {
        this.iva = iva;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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

    @JsonbTransient
    public Socio getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(Socio idSocio) {
        this.idSocio = idSocio;
    }

    public List<DetalleFactura> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleFactura> detalles) {
        this.detalles = detalles;
    }
    
    /**
     * Calculate totals from detail lines
     */
    public void calcularTotales() {
        if (detalles == null || detalles.isEmpty()) {
            this.subtotal = BigDecimal.ZERO;
            this.iva = BigDecimal.ZERO;
            this.total = BigDecimal.ZERO;
            return;
        }
        
        this.subtotal = detalles.stream()
            .map(d -> d.getSubtotal() != null ? d.getSubtotal() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // IVA rate - can be made configurable via system property or database setting
        BigDecimal ivaRate = new BigDecimal(System.getProperty("contabilidad.iva.rate", "0.13"));
        this.iva = this.subtotal.multiply(ivaRate);
        this.total = this.subtotal.add(this.iva);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFactura != null ? idFactura.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Factura)) {
            return false;
        }
        Factura other = (Factura) object;
        return !((this.idFactura == null && other.idFactura != null) 
                || (this.idFactura != null && !this.idFactura.equals(other.idFactura)));
    }

    @Override
    public String toString() {
        return "Factura[ idFactura=" + idFactura + " ]";
    }
}
