package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BalanceGeneralDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String periodo;
    private List<RubroBalanceDto> activosCorrientes = new ArrayList<>();
    private List<RubroBalanceDto> activosNoCorrientes = new ArrayList<>();
    private List<RubroBalanceDto> pasivosCorrientes = new ArrayList<>();
    private List<RubroBalanceDto> pasivosNoCorrientes = new ArrayList<>();
    private List<RubroBalanceDto> patrimonio = new ArrayList<>();
    private BigDecimal totalActivos;
    private BigDecimal totalPasivos;
    private BigDecimal totalPatrimonio;
    private BigDecimal totalPasivosPatrimonio;

    public BalanceGeneralDto() {
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public List<RubroBalanceDto> getActivosCorrientes() {
        return activosCorrientes;
    }

    public void setActivosCorrientes(List<RubroBalanceDto> activosCorrientes) {
        this.activosCorrientes = activosCorrientes;
    }

    public List<RubroBalanceDto> getActivosNoCorrientes() {
        return activosNoCorrientes;
    }

    public void setActivosNoCorrientes(List<RubroBalanceDto> activosNoCorrientes) {
        this.activosNoCorrientes = activosNoCorrientes;
    }

    public List<RubroBalanceDto> getPasivosCorrientes() {
        return pasivosCorrientes;
    }

    public void setPasivosCorrientes(List<RubroBalanceDto> pasivosCorrientes) {
        this.pasivosCorrientes = pasivosCorrientes;
    }

    public List<RubroBalanceDto> getPasivosNoCorrientes() {
        return pasivosNoCorrientes;
    }

    public void setPasivosNoCorrientes(List<RubroBalanceDto> pasivosNoCorrientes) {
        this.pasivosNoCorrientes = pasivosNoCorrientes;
    }

    public List<RubroBalanceDto> getPatrimonio() {
        return patrimonio;
    }

    public void setPatrimonio(List<RubroBalanceDto> patrimonio) {
        this.patrimonio = patrimonio;
    }

    public BigDecimal getTotalActivos() {
        return totalActivos;
    }

    public void setTotalActivos(BigDecimal totalActivos) {
        this.totalActivos = totalActivos;
    }

    public BigDecimal getTotalPasivos() {
        return totalPasivos;
    }

    public void setTotalPasivos(BigDecimal totalPasivos) {
        this.totalPasivos = totalPasivos;
    }

    public BigDecimal getTotalPatrimonio() {
        return totalPatrimonio;
    }

    public void setTotalPatrimonio(BigDecimal totalPatrimonio) {
        this.totalPatrimonio = totalPatrimonio;
    }

    public BigDecimal getTotalPasivosPatrimonio() {
        return totalPasivosPatrimonio;
    }

    public void setTotalPasivosPatrimonio(BigDecimal totalPasivosPatrimonio) {
        this.totalPasivosPatrimonio = totalPasivosPatrimonio;
    }
}
