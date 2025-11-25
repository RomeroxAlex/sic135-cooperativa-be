package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "auditoria")
@NamedQueries({
    @NamedQuery(name = "Auditoria.findAll", query = "SELECT a FROM Auditoria a ORDER BY a.fechaAccion DESC"),
    @NamedQuery(name = "Auditoria.findByUsuario", query = "SELECT a FROM Auditoria a WHERE a.usuario = :usuario ORDER BY a.fechaAccion DESC"),
    @NamedQuery(name = "Auditoria.findByEntidad", query = "SELECT a FROM Auditoria a WHERE a.entidad = :entidad ORDER BY a.fechaAccion DESC")
})
public class Auditoria implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_auditoria", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "entidad", nullable = false, length = 100)
    private String entidad;

    @Column(name = "entidad_id", length = 100)
    private String entidadId;

    @Column(name = "accion", nullable = false, length = 50)
    private String accion;

    @Column(name = "usuario", length = 100)
    private String usuario;

    @Column(name = "fecha_accion", nullable = false)
    private LocalDateTime fechaAccion;

    @Lob
    @Column(name = "datos_anteriores")
    private String datosAnteriores;

    @Lob
    @Column(name = "datos_nuevos")
    private String datosNuevos;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "detalle", length = 500)
    private String detalle;

    public Auditoria() {
    }

    public Auditoria(UUID id) {
        this.id = id;
    }

    @PrePersist
    protected void onCreate() {
        this.fechaAccion = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public String getEntidadId() {
        return entidadId;
    }

    public void setEntidadId(String entidadId) {
        this.entidadId = entidadId;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFechaAccion() {
        return fechaAccion;
    }

    public void setFechaAccion(LocalDateTime fechaAccion) {
        this.fechaAccion = fechaAccion;
    }

    public String getDatosAnteriores() {
        return datosAnteriores;
    }

    public void setDatosAnteriores(String datosAnteriores) {
        this.datosAnteriores = datosAnteriores;
    }

    public String getDatosNuevos() {
        return datosNuevos;
    }

    public void setDatosNuevos(String datosNuevos) {
        this.datosNuevos = datosNuevos;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Auditoria that = (Auditoria) obj;
        return id != null && id.equals(that.id);
    }

    @Override
    public String toString() {
        return "Auditoria{id=" + id + ", entidad='" + entidad + "', accion='" + accion + "'}";
    }
}
