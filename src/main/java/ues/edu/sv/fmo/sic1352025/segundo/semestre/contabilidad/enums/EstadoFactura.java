package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums;

public enum EstadoFactura {
    BORRADOR("Borrador"),
    EMITIDA("Emitida"),
    PAGADA("Pagada"),
    ANULADA("Anulada");

    private final String descripcion;

    EstadoFactura(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
