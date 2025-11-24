package com.tuempresa.contabilidad.service;

import com.tuempresa.contabilidad.dto.common.PagedResult;
import com.tuempresa.contabilidad.dto.cuenta.CuentaDto;
import com.tuempresa.contabilidad.entity.Cuenta;
import com.tuempresa.contabilidad.entity.TipoCuenta;
import com.tuempresa.contabilidad.repository.CuentaRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class CuentaService {

    @Inject
    private CuentaRepository cuentaRepository;

    @Inject
    private CuentaMapper cuentaMapper;

    public PagedResult<CuentaDto> getAllCuentas(int page, int size, TipoCuenta tipo) {
        List<Cuenta> cuentas = cuentaRepository.findWithFilters(page, size, tipo);
        long total = cuentaRepository.countWithFilters(tipo);
        return new PagedResult<>(
                cuentas.stream().map(cuentaMapper::toDto).collect(Collectors.toList()),
                total,
                page,
                size
        );
    }

    public Optional<CuentaDto> getCuentaById(Long id) {
        return cuentaRepository.findById(id).map(cuentaMapper::toDto);
    }

    @Transactional
    public CuentaDto createCuenta(CuentaDto cuentaDto) {
        // Business logic validation
        if (cuentaRepository.findByCodigo(cuentaDto.getCodigo()).isPresent()) {
            throw new IllegalArgumentException("El código de la cuenta ya existe.");
        }

        Cuenta cuenta = cuentaMapper.toEntity(cuentaDto);
        cuentaRepository.save(cuenta);
        return cuentaMapper.toDto(cuenta);
    }

    @Transactional
    public CuentaDto updateCuenta(Long id, CuentaDto cuentaDto) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cuenta no encontrada con id: " + id));

        // Check if the code is being changed to one that already exists
        if (!cuenta.getCodigo().equals(cuentaDto.getCodigo())) {
            if (cuentaRepository.findByCodigo(cuentaDto.getCodigo()).isPresent()) {
                throw new IllegalArgumentException("El código de la cuenta ya existe.");
            }
        }

        cuentaMapper.updateEntityFromDto(cuentaDto, cuenta);
        Cuenta updatedCuenta = cuentaRepository.update(cuenta);
        return cuentaMapper.toDto(updatedCuenta);
    }

    @Transactional
    public void deleteCuenta(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cuenta no encontrada con id: " + id));
        cuentaRepository.delete(cuenta);
    }
}
