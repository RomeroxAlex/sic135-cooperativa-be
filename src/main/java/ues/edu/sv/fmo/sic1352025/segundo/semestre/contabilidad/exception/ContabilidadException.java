package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception;

/**
 * Custom exception for accounting/business rule violations.
 */
public class ContabilidadException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public ContabilidadException(String message) {
        super(message);
    }
    
    public ContabilidadException(String message, Throwable cause) {
        super(message, cause);
    }
}
