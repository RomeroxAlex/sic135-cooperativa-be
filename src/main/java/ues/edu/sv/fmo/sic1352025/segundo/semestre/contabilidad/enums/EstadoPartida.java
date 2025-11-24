package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums;

public enum EstadoPartida {
    BORRADOR("Borrador"),
    POSTEADA("Posteada"),
    ANULADA("Anulada");

    private final String descripcion;

    EstadoPartida(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
