package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "manual_cuenta")
@NamedQueries({
    @NamedQuery(name = "ManualCuenta.findAll", query = "SELECT m FROM ManualCuenta m ORDER BY m.fechaCreacion DESC"),
    @NamedQuery(name = "ManualCuenta.findByVersion", query = "SELECT m FROM ManualCuenta m WHERE m.version = :version")
})
public class ManualCuenta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_manual_cuenta", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Lob
    @Column(name = "contenido")
    private String contenido;

    @Column(name = "version", length = 20)
    private String version;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "usuario_creacion", length = 100)
    private String usuarioCreacion;

    @Column(name = "usuario_modificacion", length = 100)
    private String usuarioModificacion;

    @Column(name = "activo")
    private Boolean activo = true;

    public ManualCuenta() {
    }

    public ManualCuenta(UUID id) {
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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ManualCuenta that = (ManualCuenta) obj;
        return id != null && id.equals(that.id);
    }

    @Override
    public String toString() {
        return "ManualCuenta{id=" + id + ", titulo='" + titulo + "', version='" + version + "'}";
    }
}
