package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "factura")
@NamedQueries({
    @NamedQuery(name = "Factura.findAll", query = "SELECT f FROM Factura f ORDER BY f.fecha DESC"),
    @NamedQuery(name = "Factura.findByFecha", query = "SELECT f FROM Factura f WHERE f.fecha = :fecha"),
    @NamedQuery(name = "Factura.findByEstado", query = "SELECT f FROM Factura f WHERE f.estado = :estado"),
    @NamedQuery(name = "Factura.findByCliente", query = "SELECT f FROM Factura f WHERE f.clienteId = :clienteId"),
    @NamedQuery(name = "Factura.countAll", query = "SELECT COUNT(f) FROM Factura f")
})
public class Factura implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_factura", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "numero_factura", nullable = false, unique = true, length = 50)
    private String numeroFactura;

    @Column(name = "cliente_id", columnDefinition = "uuid")
    private UUID clienteId;

    @Column(name = "cliente_nombre", length = 200)
    private String clienteNombre;

    @Column(name = "cliente_nit", length = 20)
    private String clienteNit;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "subtotal", precision = 18, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "impuestos", precision = 18, scale = 2)
    private BigDecimal impuestos = BigDecimal.ZERO;

    @Column(name = "total", precision = 18, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado = "BORRADOR";

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemFactura> items = new ArrayList<>();

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "usuario_creacion", length = 100)
    private String usuarioCreacion;

    @Column(name = "usuario_modificacion", length = 100)
    private String usuarioModificacion;

    public Factura() {
    }

    public Factura(UUID id) {
        this.id = id;
    }

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaModificacion = LocalDateTime.now();
    }

    public void addItem(ItemFactura item) {
        items.add(item);
        item.setFactura(this);
        recalcularTotales();
    }

    public void removeItem(ItemFactura item) {
        items.remove(item);
        item.setFactura(null);
        recalcularTotales();
    }

    public void recalcularTotales() {
        this.subtotal = items.stream()
            .map(i -> i.getSubtotal() != null ? i.getSubtotal() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.impuestos = this.subtotal.multiply(new BigDecimal("0.13"));
        this.total = this.subtotal.add(this.impuestos);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public UUID getClienteId() {
        return clienteId;
    }

    public void setClienteId(UUID clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getClienteNit() {
        return clienteNit;
    }

    public void setClienteNit(String clienteNit) {
        this.clienteNit = clienteNit;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(BigDecimal impuestos) {
        this.impuestos = impuestos;
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

    public List<ItemFactura> getItems() {
        return items;
    }

    public void setItems(List<ItemFactura> items) {
        this.items = items;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Factura that = (Factura) obj;
        return id != null && id.equals(that.id);
    }

    @Override
    public String toString() {
        return "Factura{id=" + id + ", numeroFactura='" + numeroFactura + "', total=" + total + "}";
    }
}
