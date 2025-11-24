package com.tuempresa.contabilidad.dto.cuenta;

import com.tuempresa.contabilidad.entity.TipoCuenta;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CuentaDto {

    private Long id;

    @NotEmpty(message = "El código no puede estar vacío")
    @Size(max = 20, message = "El código no puede tener más de 20 caracteres")
    private String codigo;

    @NotEmpty(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String nombre;

    @NotNull(message = "El tipo de cuenta es obligatorio")
    private TipoCuenta tipo;

    @NotNull(message = "El nivel es obligatorio")
    private Integer nivel;

    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
    private String descripcion;

    private Long cuentaPadreId;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public TipoCuenta getTipo() {
        return tipo;
    }

    public void setTipo(TipoCuenta tipo) {
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

    public Long getCuentaPadreId() {
        return cuentaPadreId;
    }

    public void setCuentaPadreId(Long cuentaPadreId) {
        this.cuentaPadreId = cuentaPadreId;
    }
}
