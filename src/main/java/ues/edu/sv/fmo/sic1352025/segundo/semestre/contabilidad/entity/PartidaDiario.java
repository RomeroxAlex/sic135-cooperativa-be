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
@Table(name = "partida_diario")
@NamedQueries({
    @NamedQuery(name = "PartidaDiario.findAll", query = "SELECT p FROM PartidaDiario p ORDER BY p.fecha DESC"),
    @NamedQuery(name = "PartidaDiario.findByFechaRange", query = "SELECT p FROM PartidaDiario p WHERE p.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY p.fecha DESC"),
    @NamedQuery(name = "PartidaDiario.findByEstado", query = "SELECT p FROM PartidaDiario p WHERE p.estado = :estado ORDER BY p.fecha DESC"),
    @NamedQuery(name = "PartidaDiario.countAll", query = "SELECT COUNT(p) FROM PartidaDiario p")
})
public class PartidaDiario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_partida_diario", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "numero_partida", nullable = false, unique = true)
    private Long numeroPartida;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "referencia", length = 100)
    private String referencia;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado = "BORRADOR";

    @Column(name = "es_ajuste")
    private Boolean esAjuste = false;

    @Column(name = "motivo_ajuste", length = 500)
    private String motivoAjuste;

    @Column(name = "total_debe", precision = 18, scale = 2)
    private BigDecimal totalDebe = BigDecimal.ZERO;

    @Column(name = "total_haber", precision = 18, scale = 2)
    private BigDecimal totalHaber = BigDecimal.ZERO;

    @OneToMany(mappedBy = "partidaDiario", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LineaPartida> lineas = new ArrayList<>();

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "usuario_creacion", length = 100)
    private String usuarioCreacion;

    @Column(name = "usuario_modificacion", length = 100)
    private String usuarioModificacion;

    public PartidaDiario() {
    }

    public PartidaDiario(UUID id) {
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

    public void addLinea(LineaPartida linea) {
        lineas.add(linea);
        linea.setPartidaDiario(this);
        recalcularTotales();
    }

    public void removeLinea(LineaPartida linea) {
        lineas.remove(linea);
        linea.setPartidaDiario(null);
        recalcularTotales();
    }

    public void recalcularTotales() {
        this.totalDebe = lineas.stream()
            .map(l -> l.getDebe() != null ? l.getDebe() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.totalHaber = lineas.stream()
            .map(l -> l.getHaber() != null ? l.getHaber() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean estaBalanceada() {
        return totalDebe.compareTo(totalHaber) == 0;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getNumeroPartida() {
        return numeroPartida;
    }

    public void setNumeroPartida(Long numeroPartida) {
        this.numeroPartida = numeroPartida;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Boolean getEsAjuste() {
        return esAjuste;
    }

    public void setEsAjuste(Boolean esAjuste) {
        this.esAjuste = esAjuste;
    }

    public String getMotivoAjuste() {
        return motivoAjuste;
    }

    public void setMotivoAjuste(String motivoAjuste) {
        this.motivoAjuste = motivoAjuste;
    }

    public BigDecimal getTotalDebe() {
        return totalDebe;
    }

    public void setTotalDebe(BigDecimal totalDebe) {
        this.totalDebe = totalDebe;
    }

    public BigDecimal getTotalHaber() {
        return totalHaber;
    }

    public void setTotalHaber(BigDecimal totalHaber) {
        this.totalHaber = totalHaber;
    }

    public List<LineaPartida> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaPartida> lineas) {
        this.lineas = lineas;
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
        PartidaDiario that = (PartidaDiario) obj;
        return id != null && id.equals(that.id);
    }

    @Override
    public String toString() {
        return "PartidaDiario{id=" + id + ", numeroPartida=" + numeroPartida + ", fecha=" + fecha + "}";
    }
}
