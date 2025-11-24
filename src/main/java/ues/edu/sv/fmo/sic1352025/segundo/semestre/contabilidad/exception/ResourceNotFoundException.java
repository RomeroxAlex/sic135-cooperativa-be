package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.exception;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceType, Object id) {
        super(resourceType + " no encontrado con id: " + id);
    }
}
