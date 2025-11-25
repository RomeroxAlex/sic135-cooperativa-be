package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class VentaDiariaDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private LocalDate fecha;
    private Long totalFacturas;
    private BigDecimal totalVentas;
    private BigDecimal totalImpuestos;
    private BigDecimal promedioVenta;

    public VentaDiariaDto() {
    }

    public VentaDiariaDto(LocalDate fecha, Long totalFacturas, BigDecimal totalVentas, 
                          BigDecimal totalImpuestos, BigDecimal promedioVenta) {
        this.fecha = fecha;
        this.totalFacturas = totalFacturas;
        this.totalVentas = totalVentas;
        this.totalImpuestos = totalImpuestos;
        this.promedioVenta = promedioVenta;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Long getTotalFacturas() {
        return totalFacturas;
    }

    public void setTotalFacturas(Long totalFacturas) {
        this.totalFacturas = totalFacturas;
    }

    public BigDecimal getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(BigDecimal totalVentas) {
        this.totalVentas = totalVentas;
    }

    public BigDecimal getTotalImpuestos() {
        return totalImpuestos;
    }

    public void setTotalImpuestos(BigDecimal totalImpuestos) {
        this.totalImpuestos = totalImpuestos;
    }

    public BigDecimal getPromedioVenta() {
        return promedioVenta;
    }

    public void setPromedioVenta(BigDecimal promedioVenta) {
        this.promedioVenta = promedioVenta;
    }
}
