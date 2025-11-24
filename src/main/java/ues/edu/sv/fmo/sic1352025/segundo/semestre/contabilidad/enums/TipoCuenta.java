package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums;

public enum TipoCuenta {
    ACTIVO("Activo"),
    PASIVO("Pasivo"),
    PATRIMONIO("Patrimonio"),
    INGRESO("Ingreso"),
    GASTO("Gasto"),
    COSTO("Costo");

    private final String descripcion;

    TipoCuenta(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
