package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums;


public enum OperacionCreditoMapper {
    APERTURA_CREDITO(TablaOrigen.CREDITO),
    CIERRE_CREDITO(TablaOrigen.CREDITO),
    APORTACION_CREDITO(TablaOrigen.CREDITO),
    APERTURA_CUENTA(TablaOrigen.CUENTA_BANCARIA),
    APORTACION_CUENTA(TablaOrigen.CUENTA_BANCARIA),
    RETIRO_CUENTA(TablaOrigen.CUENTA_BANCARIA),
    CIERRE_CUENTA(TablaOrigen.CUENTA_BANCARIA),
    APERTURA_TARJETA(TablaOrigen.CUENTA_BANCARIA),
    APORTACION_TARJETA(TablaOrigen.CUENTA_BANCARIA),
    CIERRE_TARJETA(TablaOrigen.CUENTA_BANCARIA);

    private final TablaOrigen tablaOrigen;

    OperacionCreditoMapper(TablaOrigen tablaOrigen) {
        this.tablaOrigen = tablaOrigen;
    }

    public TablaOrigen getTablaOrigen(){
        return tablaOrigen;
    }
}
/*
    OPERACIONES BANCARIAS A CONSIDERAR

        "CIERRE_CREDITO"
        "APERTURA_CUENTA"
        "RETIRO_CUENTA"
        "CIERRE_TARJETA"
        "APERTURA_CREDITO"
        "APERTURA_TARJETA"
        "APORTACION_TARJETA"
        "CIERRE_CUENTA"
        "APORTACION_CREDITO"
        "APORTACION_CUENTA"
*/