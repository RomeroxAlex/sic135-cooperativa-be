package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class CuentaDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @NotBlank(message = "El código es requerido")
    @Size(max = 20, message = "El código no puede exceder 20 caracteres")
    private String codigo;

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String nombre;

    @NotNull(message = "El tipo es requerido")
    @Size(max = 50, message = "El tipo no puede exceder 50 caracteres")
    private String tipo;

    private Integer nivel;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    private UUID cuentaPadreId;

    private LocalDateTime fechaCreacion;

    public CuentaDto() {
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

    public UUID getCuentaPadreId() {
        return cuentaPadreId;
    }

    public void setCuentaPadreId(UUID cuentaPadreId) {
        this.cuentaPadreId = cuentaPadreId;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
