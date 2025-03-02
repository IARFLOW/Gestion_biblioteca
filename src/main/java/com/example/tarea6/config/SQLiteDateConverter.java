package com.example.tarea6.config;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SQLiteDateConverter implements AttributeConverter<LocalDate, String> {

    @Override
    public String convertToDatabaseColumn(LocalDate attribute) {
        return attribute == null ? null : attribute.toString();
    }

    @Override
    public LocalDate convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        
        try {
            // Intenta analizar como una fecha en formato estándar (YYYY-MM-DD)
            return LocalDate.parse(dbData);
        } catch (Exception e) {
            try {
                // Si falla, intenta analizar como timestamp en milisegundos
                long timestamp = Long.parseLong(dbData);
                return Instant.ofEpochMilli(timestamp)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
            } catch (Exception e2) {
                // Si ambas opciones fallan, registra el error y devuelve null
                System.err.println("Error al convertir fecha: " + dbData + ", error: " + e2.getMessage());
                return null;
            }
        }
    }
}