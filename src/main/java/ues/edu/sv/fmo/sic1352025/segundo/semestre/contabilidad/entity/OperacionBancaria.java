/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import java.io.Serializable;
import java.util.Collection;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 *
 * @author jordi
 */
@Entity
@Table(name = "operacion_bancaria")
@NamedQueries({
    @NamedQuery(name = "OperacionBancaria.findAll", query = "SELECT o FROM OperacionBancaria o"),
    @NamedQuery(name = "OperacionBancaria.findByNombre", query = "SELECT o FROM OperacionBancaria o WHERE o.nombre = :nombre"),
    @NamedQuery(name = "OperacionBancaria.findByDescripcion", query = "SELECT o FROM OperacionBancaria o WHERE o.descripcion = :descripcion"),
    @NamedQuery(name = "OperacionBancaria.findByNaturaleza", query = "SELECT o FROM OperacionBancaria o WHERE o.naturaleza = :naturaleza"),
    @NamedQuery(name = "OperacionBancaria.findByEstado", query = "SELECT o FROM OperacionBancaria o WHERE o.estado = :estado"),
    @NamedQuery(name = "OperacionBancaria.findByDebeHaber", query = "SELECT o FROM OperacionBancaria o WHERE o.debeHaber = :debeHaber")})
public class OperacionBancaria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Lob
    @Column(name = "id_operacion_bancaria")
    private Object idOperacionBancaria;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "naturaleza")
    private String naturaleza;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "debe_haber")
    private String debeHaber;
    @OneToMany(mappedBy = "idOperacionBancaria")
    private Collection<CabeceraTransaccion> cabeceraTransaccionCollection;
    @OneToMany(mappedBy = "idOperacionBancaria")
    private Collection<CuentaOperacion> cuentaOperacionCollection;

    public OperacionBancaria() {
    }

    public OperacionBancaria(Object idOperacionBancaria) {
        this.idOperacionBancaria = idOperacionBancaria;
    }

    public Object getIdOperacionBancaria() {
        return idOperacionBancaria;
    }

    public void setIdOperacionBancaria(Object idOperacionBancaria) {
        this.idOperacionBancaria = idOperacionBancaria;
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

    public String getNaturaleza() {
        return naturaleza;
    }

    public void setNaturaleza(String naturaleza) {
        this.naturaleza = naturaleza;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getDebeHaber() {
        return debeHaber;
    }

    public void setDebeHaber(String debeHaber) {
        this.debeHaber = debeHaber;
    }

    public Collection<CabeceraTransaccion> getCabeceraTransaccionCollection() {
        return cabeceraTransaccionCollection;
    }

    public void setCabeceraTransaccionCollection(Collection<CabeceraTransaccion> cabeceraTransaccionCollection) {
        this.cabeceraTransaccionCollection = cabeceraTransaccionCollection;
    }

    public Collection<CuentaOperacion> getCuentaOperacionCollection() {
        return cuentaOperacionCollection;
    }

    public void setCuentaOperacionCollection(Collection<CuentaOperacion> cuentaOperacionCollection) {
        this.cuentaOperacionCollection = cuentaOperacionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idOperacionBancaria != null ? idOperacionBancaria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OperacionBancaria)) {
            return false;
        }
        OperacionBancaria other = (OperacionBancaria) object;
        if ((this.idOperacionBancaria == null && other.idOperacionBancaria != null) || (this.idOperacionBancaria != null && !this.idOperacionBancaria.equals(other.idOperacionBancaria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.OperacionBancaria[ idOperacionBancaria=" + idOperacionBancaria + " ]";
    }
    
}
