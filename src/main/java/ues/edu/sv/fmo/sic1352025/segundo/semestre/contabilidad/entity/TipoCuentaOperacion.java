package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipo_cuenta_operacion")
public class TipoCuentaOperacion implements Serializable{

    @Id
    @Column(name = "id_tipo_cuenta_operacion")
    private UUID idTipoCuentaOperacion;

    @Column(name = "nombre")
    private String nombre;

    public TipoCuentaOperacion(){}

    public TipoCuentaOperacion(UUID idTipoCuentaOperacion){
        this.idTipoCuentaOperacion = idTipoCuentaOperacion;
    }


    public UUID getIdTipoCuentaOperacion() {
        return idTipoCuentaOperacion;
    }

    public void setIdTipoCuentaOperacion(UUID idTipoCuentaOperacion) {
        this.idTipoCuentaOperacion = idTipoCuentaOperacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    

}
