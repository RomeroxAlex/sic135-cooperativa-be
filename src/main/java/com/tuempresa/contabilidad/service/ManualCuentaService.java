package com.tuempresa.contabilidad.service;

import com.tuempresa.contabilidad.dto.ManualDto;
import com.tuempresa.contabilidad.entity.ManualCuenta;
import com.tuempresa.contabilidad.repository.ManualCuentaRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ManualCuentaService {

    @Inject
    private ManualCuentaRepository manualRepository;

    @Inject
    private ManualCuentaMapper manualMapper;

    public List<ManualDto> getAllManuales() {
        return manualRepository.findAll().stream()
                .map(manualMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ManualDto createManual(ManualDto manualDto) {
        ManualCuenta manual = manualMapper.toEntity(manualDto);
        manual.setFechaCreacion(LocalDate.now());
        manualRepository.save(manual);
        return manualMapper.toDto(manual);
    }

    @Transactional
    public ManualDto updateManual(Long id, ManualDto manualDto) {
        ManualCuenta manual = manualRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Manual no encontrado con id: " + id));

        manualMapper.updateEntityFromDto(manualDto, manual);
        ManualCuenta updatedManual = manualRepository.update(manual);
        return manualMapper.toDto(updatedManual);
    }
}
