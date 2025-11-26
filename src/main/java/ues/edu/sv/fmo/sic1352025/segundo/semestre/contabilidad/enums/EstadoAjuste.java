package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.enums;

/**
 * Status of adjustment entries (Estado de Ajuste).
 */
public enum EstadoAjuste {
    PENDIENTE,  // Pending - not yet applied
    APLICADO,   // Applied - journal entry generated
    ANULADO     // Voided - cancelled
}
