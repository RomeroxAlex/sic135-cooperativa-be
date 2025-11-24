package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class ManualDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @NotBlank(message = "El título es requerido")
    @Size(max = 200, message = "El título no puede exceder 200 caracteres")
    private String titulo;

    private String contenido;

    @Size(max = 20, message = "La versión no puede exceder 20 caracteres")
    private String version;

    private LocalDateTime fechaCreacion;

    public ManualDto() {
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
}
