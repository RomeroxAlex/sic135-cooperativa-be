package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums;

/**
 * Status of journal entries (Estado de Partida).
 */
public enum EstadoPartida {
    BORRADOR,       // Draft - can be edited
    CONTABILIZADA,  // Posted - cannot be edited, affects balances
    ANULADA         // Voided - marked as cancelled
}
