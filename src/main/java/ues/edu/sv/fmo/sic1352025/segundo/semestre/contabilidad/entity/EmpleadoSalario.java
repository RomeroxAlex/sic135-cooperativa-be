/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "empleado_salario")
@NamedQueries({
    @NamedQuery(name = "EmpleadoSalario.findAll", query = "SELECT e FROM EmpleadoSalario e"),
    @NamedQuery(name = "EmpleadoSalario.findBySalario", query = "SELECT e FROM EmpleadoSalario e WHERE e.salario = :salario")})
public class EmpleadoSalario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_empleado_salario")
    private UUID idEmpleadoSalario;
    @Column(name = "salario")
    private BigInteger salario;
    @JoinColumn(name = "id_empleado", referencedColumnName = "id_empleado")
    @ManyToOne
    private Empleado idEmpleado;

    public EmpleadoSalario() {
    }

    public EmpleadoSalario(UUID idEmpleadoSalario) {
        this.idEmpleadoSalario = idEmpleadoSalario;
    }

    public UUID getIdEmpleadoSalario() {
        return idEmpleadoSalario;
    }

    public void setIdEmpleadoSalario(UUID idEmpleadoSalario) {
        this.idEmpleadoSalario = idEmpleadoSalario;
    }

    public BigInteger getSalario() {
        return salario;
    }

    public void setSalario(BigInteger salario) {
        this.salario = salario;
    }

    public Empleado getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Empleado idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEmpleadoSalario != null ? idEmpleadoSalario.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.EmpleadoSalario[ idEmpleadoSalario=" + idEmpleadoSalario + " ]";
    }
    
}
