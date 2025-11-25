#!/bin/bash
# Script de inicialización para restaurar backup de PostgreSQL
# Este script se ejecuta automáticamente cuando el contenedor inicia
# y el volumen de datos está vacío (primera ejecución)

set -e

echo "========================================"
echo "  Iniciando restauración de la base de datos"
echo "========================================"

# Verificar que las variables de entorno necesarias estén definidas
if [ -z "$POSTGRES_USER" ]; then
    echo "Error: POSTGRES_USER no está definido"
    exit 1
fi

if [ -z "$POSTGRES_DB" ]; then
    echo "Error: POSTGRES_DB no está definido"
    exit 1
fi

# Verificar que el archivo de backup existe
if [ ! -f /backup.dump ]; then
    echo "Error: El archivo /backup.dump no existe"
    exit 1
fi

echo "Usuario: $POSTGRES_USER"
echo "Base de datos: $POSTGRES_DB"
echo "Archivo de backup: /backup.dump"

# Restaurar la base de datos usando pg_restore
echo "Restaurando backup..."
pg_restore -U "$POSTGRES_USER" -d "$POSTGRES_DB" /backup.dump || {
    echo "Advertencia: pg_restore terminó con algunos errores (puede ser normal si hay objetos existentes)"
}

echo "========================================"
echo "  Restauración completada exitosamente"
echo "========================================"
