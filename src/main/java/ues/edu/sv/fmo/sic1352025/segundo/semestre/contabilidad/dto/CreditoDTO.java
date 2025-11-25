package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.dto;

import java.util.UUID;

import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.Credito;

public class CreditoDTO {

    private UUID idSocio;
    private Credito credito;
    
    public UUID getIdSocio() {
        return idSocio;
    }
    public void setIdSocio(UUID idSocio) {
        this.idSocio = idSocio;
    }
    public Credito getCredito() {
        return credito;
    }
    public void setCredito(Credito credito) {
        this.credito = credito;
    }

    
}
