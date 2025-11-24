package com.tuempresa.contabilidad.service;

import com.tuempresa.contabilidad.dto.cuenta.CuentaDto;
import com.tuempresa.contabilidad.entity.Cuenta;
import com.tuempresa.contabilidad.repository.CuentaRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class CuentaMapper {

    @Inject
    private CuentaRepository cuentaRepository;

    public CuentaDto toDto(Cuenta cuenta) {
        if (cuenta == null) {
            return null;
        }
        CuentaDto dto = new CuentaDto();
        dto.setId(cuenta.getId());
        dto.setCodigo(cuenta.getCodigo());
        dto.setNombre(cuenta.getNombre());
        dto.setTipo(cuenta.getTipo());
        dto.setNivel(cuenta.getNivel());
        dto.setDescripcion(cuenta.getDescripcion());
        if (cuenta.getCuentaPadre() != null) {
            dto.setCuentaPadreId(cuenta.getCuentaPadre().getId());
        }
        return dto;
    }

    public Cuenta toEntity(CuentaDto dto) {
        if (dto == null) {
            return null;
        }
        Cuenta cuenta = new Cuenta();
        cuenta.setId(dto.getId());
        cuenta.setCodigo(dto.getCodigo());
        cuenta.setNombre(dto.getNombre());
        cuenta.setTipo(dto.getTipo());
        cuenta.setNivel(dto.getNivel());
        cuenta.setDescripcion(dto.getDescripcion());

        if (dto.getCuentaPadreId() != null) {
            Optional<Cuenta> cuentaPadre = cuentaRepository.findById(dto.getCuentaPadreId());
            cuentaPadre.ifPresent(cuenta::setCuentaPadre);
        }

        return cuenta;
    }

    public void updateEntityFromDto(CuentaDto dto, Cuenta entity) {
        entity.setCodigo(dto.getCodigo());
        entity.setNombre(dto.getNombre());
        entity.setTipo(dto.getTipo());
        entity.setNivel(dto.getNivel());
        entity.setDescripcion(dto.getDescripcion());

        if (dto.getCuentaPadreId() != null) {
            if (entity.getCuentaPadre() == null || !entity.getCuentaPadre().getId().equals(dto.getCuentaPadreId())) {
                 Optional<Cuenta> cuentaPadre = cuentaRepository.findById(dto.getCuentaPadreId());
                 cuentaPadre.ifPresent(entity::setCuentaPadre);
            }
        } else {
            entity.setCuentaPadre(null);
        }
    }
}
