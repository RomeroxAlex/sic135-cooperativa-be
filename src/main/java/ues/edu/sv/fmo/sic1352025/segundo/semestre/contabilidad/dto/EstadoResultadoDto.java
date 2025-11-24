package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EstadoResultadoDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String periodo;
    private List<RubroBalanceDto> ingresos = new ArrayList<>();
    private List<RubroBalanceDto> costos = new ArrayList<>();
    private List<RubroBalanceDto> gastos = new ArrayList<>();
    private BigDecimal totalIngresos;
    private BigDecimal totalCostos;
    private BigDecimal totalGastos;
    private BigDecimal utilidadBruta;
    private BigDecimal utilidadNeta;

    public EstadoResultadoDto() {
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public List<RubroBalanceDto> getIngresos() {
        return ingresos;
    }

    public void setIngresos(List<RubroBalanceDto> ingresos) {
        this.ingresos = ingresos;
    }

    public List<RubroBalanceDto> getCostos() {
        return costos;
    }

    public void setCostos(List<RubroBalanceDto> costos) {
        this.costos = costos;
    }

    public List<RubroBalanceDto> getGastos() {
        return gastos;
    }

    public void setGastos(List<RubroBalanceDto> gastos) {
        this.gastos = gastos;
    }

    public BigDecimal getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(BigDecimal totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    public BigDecimal getTotalCostos() {
        return totalCostos;
    }

    public void setTotalCostos(BigDecimal totalCostos) {
        this.totalCostos = totalCostos;
    }

    public BigDecimal getTotalGastos() {
        return totalGastos;
    }

    public void setTotalGastos(BigDecimal totalGastos) {
        this.totalGastos = totalGastos;
    }

    public BigDecimal getUtilidadBruta() {
        return utilidadBruta;
    }

    public void setUtilidadBruta(BigDecimal utilidadBruta) {
        this.utilidadBruta = utilidadBruta;
    }

    public BigDecimal getUtilidadNeta() {
        return utilidadNeta;
    }

    public void setUtilidadNeta(BigDecimal utilidadNeta) {
        this.utilidadNeta = utilidadNeta;
    }
}
