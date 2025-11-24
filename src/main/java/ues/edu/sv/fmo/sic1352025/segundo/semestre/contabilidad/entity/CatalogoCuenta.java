package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "catalogo_cuenta")
@NamedQueries({
    @NamedQuery(name = "CatalogoCuenta.findAll", query = "SELECT c FROM CatalogoCuenta c WHERE c.activo = true"),
    @NamedQuery(name = "CatalogoCuenta.findByCodigo", query = "SELECT c FROM CatalogoCuenta c WHERE c.codigo = :codigo AND c.activo = true"),
    @NamedQuery(name = "CatalogoCuenta.findByTipo", query = "SELECT c FROM CatalogoCuenta c WHERE c.tipo = :tipo AND c.activo = true"),
    @NamedQuery(name = "CatalogoCuenta.countAll", query = "SELECT COUNT(c) FROM CatalogoCuenta c WHERE c.activo = true")
})
public class CatalogoCuenta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_catalogo_cuenta", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "codigo", nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    @Column(name = "nivel")
    private Integer nivel;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cuenta_padre", referencedColumnName = "id_catalogo_cuenta")
    private CatalogoCuenta cuentaPadre;

    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "usuario_creacion", length = 100)
    private String usuarioCreacion;

    @Column(name = "usuario_modificacion", length = 100)
    private String usuarioModificacion;

    public CatalogoCuenta() {
    }

    public CatalogoCuenta(UUID id) {
        this.id = id;
    }

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        this.activo = true;
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaModificacion = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public CatalogoCuenta getCuentaPadre() {
        return cuentaPadre;
    }

    public void setCuentaPadre(CatalogoCuenta cuentaPadre) {
        this.cuentaPadre = cuentaPadre;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
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
        CatalogoCuenta that = (CatalogoCuenta) obj;
        return id != null && id.equals(that.id);
    }

    @Override
    public String toString() {
        return "CatalogoCuenta{id=" + id + ", codigo='" + codigo + "', nombre='" + nombre + "'}";
    }
}
