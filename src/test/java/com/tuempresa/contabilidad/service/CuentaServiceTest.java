package com.tuempresa.contabilidad.service;

import com.tuempresa.contabilidad.dto.cuenta.CuentaDto;
import com.tuempresa.contabilidad.entity.Cuenta;
import com.tuempresa.contabilidad.entity.TipoCuenta;
import com.tuempresa.contabilidad.repository.CuentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.ws.rs.NotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CuentaServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private CuentaMapper cuentaMapper;

    @InjectMocks
    private CuentaService cuentaService;

    private Cuenta cuenta;
    private CuentaDto cuentaDto;

    @BeforeEach
    void setUp() {
        cuenta = new Cuenta();
        cuenta.setId(1L);
        cuenta.setCodigo("101");
        cuenta.setNombre("Caja");
        cuenta.setTipo(TipoCuenta.ACTIVO);
        cuenta.setNivel(1);

        cuentaDto = new CuentaDto();
        cuentaDto.setId(1L);
        cuentaDto.setCodigo("101");
        cuentaDto.setNombre("Caja");
        cuentaDto.setTipo(TipoCuenta.ACTIVO);
        cuentaDto.setNivel(1);
    }

    @Test
    void getAllCuentas_shouldReturnDtoList() {
        when(cuentaRepository.findWithFilters(1, 10, null)).thenReturn(Collections.singletonList(cuenta));
        when(cuentaRepository.countWithFilters(null)).thenReturn(1L);
        when(cuentaMapper.toDto(any(Cuenta.class))).thenReturn(cuentaDto);

        var result = cuentaService.getAllCuentas(1, 10, null);

        assertNotNull(result);
        assertEquals(1, result.getData().size());
        verify(cuentaRepository).findWithFilters(1, 10, null);
    }

    @Test
    void getCuentaById_shouldReturnDto_whenFound() {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(cuentaMapper.toDto(cuenta)).thenReturn(cuentaDto);

        Optional<CuentaDto> result = cuentaService.getCuentaById(1L);

        assertTrue(result.isPresent());
        assertEquals(cuentaDto.getId(), result.get().getId());
        verify(cuentaRepository).findById(1L);
    }

    @Test
    void getCuentaById_shouldReturnEmpty_whenNotFound() {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<CuentaDto> result = cuentaService.getCuentaById(1L);

        assertFalse(result.isPresent());
        verify(cuentaRepository).findById(1L);
    }

    @Test
    void createCuenta_shouldCreateSuccessfully() {
        when(cuentaRepository.findByCodigo("101")).thenReturn(Optional.empty());
        when(cuentaMapper.toEntity(any(CuentaDto.class))).thenReturn(cuenta);
        when(cuentaMapper.toDto(any(Cuenta.class))).thenReturn(cuentaDto);

        CuentaDto result = cuentaService.createCuenta(cuentaDto);

        assertNotNull(result);
        assertEquals(cuentaDto.getCodigo(), result.getCodigo());
        verify(cuentaRepository).save(any(Cuenta.class));
    }

    @Test
    void createCuenta_shouldThrowException_whenCodeExists() {
        when(cuentaRepository.findByCodigo("101")).thenReturn(Optional.of(cuenta));

        assertThrows(IllegalArgumentException.class, () -> cuentaService.createCuenta(cuentaDto));

        verify(cuentaRepository, never()).save(any(Cuenta.class));
    }

    @Test
    void updateCuenta_shouldUpdateSuccessfully() {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.update(any(Cuenta.class))).thenReturn(cuenta);
        when(cuentaMapper.toDto(any(Cuenta.class))).thenReturn(cuentaDto);

        CuentaDto result = cuentaService.updateCuenta(1L, cuentaDto);

        assertNotNull(result);
        verify(cuentaRepository).update(any(Cuenta.class));
    }

    @Test
    void updateCuenta_shouldThrowNotFoundException() {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> cuentaService.updateCuenta(1L, cuentaDto));

        verify(cuentaRepository, never()).update(any(Cuenta.class));
    }

    @Test
    void deleteCuenta_shouldDeleteSuccessfully() {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        doNothing().when(cuentaRepository).delete(any(Cuenta.class));

        assertDoesNotThrow(() -> cuentaService.deleteCuenta(1L));

        verify(cuentaRepository).delete(any(Cuenta.class));
    }

    @Test
    void deleteCuenta_shouldThrowNotFoundException() {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> cuentaService.deleteCuenta(1L));

        verify(cuentaRepository, never()).delete(any(Cuenta.class));
    }
}
