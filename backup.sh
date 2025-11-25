#!/bin/bash

# Script de backup para base de datos PostgreSQL
# Diseñado para funcionar de una vez con Docker

set -e

# Configuración desde variables de entorno o valores por defecto
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-5432}"
DB_USER="${DB_USER:-postgres}"
DB_NAME="${DB_NAME:-contabilidad}"
BACKUP_DIR="${BACKUP_DIR:-/backups}"

# Usar PGPASSWORD para evitar exponer contraseña en línea de comandos
export PGPASSWORD="${DB_PASSWORD:-}"

# Crear nombre del archivo con timestamp
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
BACKUP_FILE="${BACKUP_DIR}/${DB_NAME}_backup_${TIMESTAMP}.sql"

# Crear directorio de backups si no existe
mkdir -p "${BACKUP_DIR}"

echo "============================================"
echo "  Iniciando backup de base de datos"
echo "============================================"
echo "Fecha: $(date)"
echo "Base de datos: ${DB_NAME}"
echo "Host: ${DB_HOST}:${DB_PORT}"
echo "Usuario: ${DB_USER}"
echo "Archivo de backup: ${BACKUP_FILE}"
echo "============================================"

# Verificar conexión a la base de datos
echo "Verificando conexión a la base de datos..."
if pg_isready -h "${DB_HOST}" -p "${DB_PORT}" -U "${DB_USER}" -d "${DB_NAME}" -q 2>/dev/null; then
    echo "✓ Conexión exitosa a la base de datos"
else
    echo "✗ Error: No se puede conectar a la base de datos"
    echo "  Verifique los parámetros de conexión:"
    echo "  - DB_HOST: ${DB_HOST}"
    echo "  - DB_PORT: ${DB_PORT}"
    echo "  - DB_USER: ${DB_USER}"
    echo "  - DB_NAME: ${DB_NAME}"
    exit 1
fi

# Realizar el backup (sin compresión, formato SQL directo)
echo "Realizando backup..."
pg_dump \
    -h "${DB_HOST}" \
    -p "${DB_PORT}" \
    -U "${DB_USER}" \
    -d "${DB_NAME}" \
    --clean \
    --if-exists \
    --create \
    -f "${BACKUP_FILE}"

# Verificar que el backup se creó correctamente
if [ -f "${BACKUP_FILE}" ] && [ -s "${BACKUP_FILE}" ]; then
    BACKUP_SIZE=$(du -h "${BACKUP_FILE}" | cut -f1)
    echo "============================================"
    echo "  Backup completado exitosamente"
    echo "============================================"
    echo "Archivo: ${BACKUP_FILE}"
    echo "Tamaño: ${BACKUP_SIZE}"
    echo "Fecha de finalización: $(date)"
    echo "============================================"
    
    # Listar backups existentes
    echo ""
    echo "Backups disponibles en ${BACKUP_DIR}:"
    find "${BACKUP_DIR}" -maxdepth 1 -name "*.sql" -type f -exec ls -lh {} \; 2>/dev/null || echo "  (ninguno)"
else
    echo "✗ Error: El backup falló o el archivo está vacío"
    exit 1
fi

echo ""
echo "¡Proceso de backup finalizado!"
