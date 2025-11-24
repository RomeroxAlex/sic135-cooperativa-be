/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 *
 * @author jordi
 */
@Entity
@Table(name = "tipo_credito")
@NamedQueries({
    @NamedQuery(name = "TipoCredito.findAll", query = "SELECT t FROM TipoCredito t"),
    @NamedQuery(name = "TipoCredito.findByNombre", query = "SELECT t FROM TipoCredito t WHERE t.nombre = :nombre"),
    @NamedQuery(name = "TipoCredito.findByDescripcion", query = "SELECT t FROM TipoCredito t WHERE t.descripcion = :descripcion"),
    @NamedQuery(name = "TipoCredito.findByDocumentacion", query = "SELECT t FROM TipoCredito t WHERE t.documentacion = :documentacion")})
public class TipoCredito implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_tipo_credito")
    private UUID idTipoCredito;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "documentacion")
    private String documentacion;
    @OneToMany(mappedBy = "idTipoCredito")
    private Collection<TipoCreditoCaracteristica> tipoCreditoCaracteristicaCollection;
    @OneToMany(mappedBy = "idTipoCredito")
    private Collection<Credito> creditoCollection;

    public TipoCredito() {
    }

    public TipoCredito(UUID idTipoCredito) {
        this.idTipoCredito = idTipoCredito;
    }

    public UUID getIdTipoCredito() {
        return idTipoCredito;
    }

    public void setIdTipoCredito(UUID idTipoCredito) {
        this.idTipoCredito = idTipoCredito;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDocumentacion() {
        return documentacion;
    }

    public void setDocumentacion(String documentacion) {
        this.documentacion = documentacion;
    }

    public Collection<TipoCreditoCaracteristica> getTipoCreditoCaracteristicaCollection() {
        return tipoCreditoCaracteristicaCollection;
    }

    public void setTipoCreditoCaracteristicaCollection(Collection<TipoCreditoCaracteristica> tipoCreditoCaracteristicaCollection) {
        this.tipoCreditoCaracteristicaCollection = tipoCreditoCaracteristicaCollection;
    }

    public Collection<Credito> getCreditoCollection() {
        return creditoCollection;
    }

    public void setCreditoCollection(Collection<Credito> creditoCollection) {
        this.creditoCollection = creditoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoCredito != null ? idTipoCredito.hashCode() : 0);
        return hash;
    }


    @Override
    public String toString() {
        return "ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.TipoCredito[ idTipoCredito=" + idTipoCredito + " ]";
    }
    
}
