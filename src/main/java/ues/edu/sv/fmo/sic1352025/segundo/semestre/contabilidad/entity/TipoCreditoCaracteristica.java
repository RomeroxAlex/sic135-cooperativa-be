/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 *
 * @author jordi
 */
@Entity
@Table(name = "tipo_credito_caracteristica")
@NamedQueries({
    @NamedQuery(name = "TipoCreditoCaracteristica.findAll", query = "SELECT t FROM TipoCreditoCaracteristica t"),
    @NamedQuery(name = "TipoCreditoCaracteristica.findByValor", query = "SELECT t FROM TipoCreditoCaracteristica t WHERE t.valor = :valor")})
public class TipoCreditoCaracteristica implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_tipo_credito_caracteristica")
    private UUID idTipoCreditoCaracteristica;
    @Column(name = "valor")
    private String valor;
    @JoinColumn(name = "id_metadato_tipo_credito", referencedColumnName = "id_metadato_tipo_credito")
    @ManyToOne
    private MetadatoTipoCredito idMetadatoTipoCredito;
    @JoinColumn(name = "id_tipo_credito", referencedColumnName = "id_tipo_credito")
    @ManyToOne
    private TipoCredito idTipoCredito;

    public TipoCreditoCaracteristica() {
    }

    public TipoCreditoCaracteristica(UUID idTipoCreditoCaracteristica) {
        this.idTipoCreditoCaracteristica = idTipoCreditoCaracteristica;
    }

    public UUID getIdTipoCreditoCaracteristica() {
        return idTipoCreditoCaracteristica;
    }

    public void setIdTipoCreditoCaracteristica(UUID idTipoCreditoCaracteristica) {
        this.idTipoCreditoCaracteristica = idTipoCreditoCaracteristica;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public MetadatoTipoCredito getIdMetadatoTipoCredito() {
        return idMetadatoTipoCredito;
    }

    public void setIdMetadatoTipoCredito(MetadatoTipoCredito idMetadatoTipoCredito) {
        this.idMetadatoTipoCredito = idMetadatoTipoCredito;
    }

    public TipoCredito getIdTipoCredito() {
        return idTipoCredito;
    }

    public void setIdTipoCredito(TipoCredito idTipoCredito) {
        this.idTipoCredito = idTipoCredito;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoCreditoCaracteristica != null ? idTipoCreditoCaracteristica.hashCode() : 0);
        return hash;
    }

    
    @Override
    public String toString() {
        return "ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.TipoCreditoCaracteristica[ idTipoCreditoCaracteristica=" + idTipoCreditoCaracteristica + " ]";
    }
    
}
