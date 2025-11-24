package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.UUID;

@Converter(autoApply = true)
public class UuidConverter implements AttributeConverter<UUID, Object> {

    @Override
    public Object convertToDatabaseColumn(UUID attribute) {
        return attribute; // Pasa el UUID nativo al driver
    }

    @Override
    public UUID convertToEntityAttribute(Object dbData) {
        if (dbData == null) return null;
        if (dbData instanceof UUID) return (UUID) dbData;
        if (dbData instanceof String) return UUID.fromString((String) dbData);
        return UUID.fromString(dbData.toString());
    }
}