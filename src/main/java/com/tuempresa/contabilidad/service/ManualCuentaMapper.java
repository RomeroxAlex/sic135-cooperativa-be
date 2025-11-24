package com.tuempresa.contabilidad.service;

import com.tuempresa.contabilidad.dto.ManualDto;
import com.tuempresa.contabilidad.entity.ManualCuenta;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ManualCuentaMapper {

    public ManualDto toDto(ManualCuenta manual) {
        if (manual == null) {
            return null;
        }
        ManualDto dto = new ManualDto();
        dto.setId(manual.getId());
        dto.setTitulo(manual.getTitulo());
        dto.setContenido(manual.getContenido());
        dto.setVersion(manual.getVersion());
        dto.setFechaCreacion(manual.getFechaCreacion());
        return dto;
    }

    public ManualCuenta toEntity(ManualDto dto) {
        if (dto == null) {
            return null;
        }
        ManualCuenta manual = new ManualCuenta();
        manual.setId(dto.getId());
        manual.setTitulo(dto.getTitulo());
        manual.setContenido(dto.getContenido());
        manual.setVersion(dto.getVersion());
        manual.setFechaCreacion(dto.getFechaCreacion());
        return manual;
    }

    public void updateEntityFromDto(ManualDto dto, ManualCuenta entity) {
        entity.setTitulo(dto.getTitulo());
        entity.setContenido(dto.getContenido());
        entity.setVersion(dto.getVersion());
    }
}
