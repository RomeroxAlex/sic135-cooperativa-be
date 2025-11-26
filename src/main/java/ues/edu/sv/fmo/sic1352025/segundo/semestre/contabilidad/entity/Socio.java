/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 *
 * @author jordi
 */
@Entity
@Table(name = "socio")
@NamedQueries({
    @NamedQuery(name = "Socio.findAll", query = "SELECT s FROM Socio s"),
    @NamedQuery(name = "Socio.findByDocumentoFiscal", query = "SELECT s FROM Socio s WHERE s.documentoFiscal = :documentoFiscal")})
public class Socio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_socio")
    private UUID idSocio;
    @Column(name = "documento_fiscal")
    private String documentoFiscal;

    @OneToMany(mappedBy = "idSocio")
    private Collection<CreditoSocio> creditoSocioCollection;

    @JoinColumn(name = "id_persona", referencedColumnName = "id_persona")
    @ManyToOne
    private Persona idPersona;

    @OneToMany(mappedBy = "idSocio")
    private Collection<CuentaBancaria> cuentaBancariaCollection;

    public Socio() {
    }

    public Socio(UUID idSocio) {
        this.idSocio = idSocio;
    }

    public UUID getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(UUID idSocio) {
        this.idSocio = idSocio;
    }

    public String getDocumentoFiscal() {
        return documentoFiscal;
    }

    public void setDocumentoFiscal(String documentoFiscal) {
        this.documentoFiscal = documentoFiscal;
    }

    @JsonbTransient
    public Collection<CreditoSocio> getCreditoSocioCollection() {
        return creditoSocioCollection;
    }

    public void setCreditoSocioCollection(Collection<CreditoSocio> creditoSocioCollection) {
        this.creditoSocioCollection = creditoSocioCollection;
    }

    public Persona getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Persona idPersona) {
        this.idPersona = idPersona;
    }

    @JsonbTransient
    public Collection<CuentaBancaria> getCuentaBancariaCollection() {
        return cuentaBancariaCollection;
    }

    public void setCuentaBancariaCollection(Collection<CuentaBancaria> cuentaBancariaCollection) {
        this.cuentaBancariaCollection = cuentaBancariaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSocio != null ? idSocio.hashCode() : 0);
        return hash;
    }


    @Override
    public String toString() {
        return "ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.Socio[ idSocio=" + idSocio + " ]";
    }
    
}
