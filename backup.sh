#!/bin/bash

# Script de backup para base de datos MySQL/MariaDB
# Diseñado para funcionar de una vez con Docker

set -e

# Configuración desde variables de entorno o valores por defecto
DB_HOST="${DB_HOST:-localhost}"
DB_PORT="${DB_PORT:-3306}"
DB_USER="${DB_USER:-root}"
DB_NAME="${DB_NAME:-contabilidad}"
BACKUP_DIR="${BACKUP_DIR:-/backups}"

# Usar MYSQL_PWD para evitar exponer contraseña en línea de comandos
export MYSQL_PWD="${DB_PASSWORD:-}"

# Crear nombre del archivo con timestamp
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
BACKUP_FILE="${BACKUP_DIR}/${DB_NAME}_backup_${TIMESTAMP}.sql"
BACKUP_FILE_GZ="${BACKUP_FILE}.gz"

# Crear directorio de backups si no existe
mkdir -p "${BACKUP_DIR}"

echo "============================================"
echo "  Iniciando backup de base de datos"
echo "============================================"
echo "Fecha: $(date)"
echo "Base de datos: ${DB_NAME}"
echo "Host: ${DB_HOST}:${DB_PORT}"
echo "Usuario: ${DB_USER}"
echo "Archivo de backup: ${BACKUP_FILE_GZ}"
echo "============================================"

# Verificar conexión a la base de datos
echo "Verificando conexión a la base de datos..."
if mysqladmin ping -h "${DB_HOST}" -P "${DB_PORT}" -u "${DB_USER}" --silent 2>/dev/null; then
    echo "✓ Conexión exitosa a la base de datos"
else
    echo "✗ Error: No se puede conectar a la base de datos"
    echo "  Verifique los parámetros de conexión:"
    echo "  - DB_HOST: ${DB_HOST}"
    echo "  - DB_PORT: ${DB_PORT}"
    echo "  - DB_USER: ${DB_USER}"
    exit 1
fi

# Realizar el backup
echo "Realizando backup..."
mysqldump \
    -h "${DB_HOST}" \
    -P "${DB_PORT}" \
    -u "${DB_USER}" \
    --single-transaction \
    --routines \
    --triggers \
    --add-drop-database \
    --databases "${DB_NAME}" | gzip > "${BACKUP_FILE_GZ}"

# Verificar que el backup se creó correctamente
if [ -f "${BACKUP_FILE_GZ}" ] && [ -s "${BACKUP_FILE_GZ}" ]; then
    BACKUP_SIZE=$(du -h "${BACKUP_FILE_GZ}" | cut -f1)
    echo "============================================"
    echo "  Backup completado exitosamente"
    echo "============================================"
    echo "Archivo: ${BACKUP_FILE_GZ}"
    echo "Tamaño: ${BACKUP_SIZE}"
    echo "Fecha de finalización: $(date)"
    echo "============================================"
    
    # Listar backups existentes
    echo ""
    echo "Backups disponibles en ${BACKUP_DIR}:"
    find "${BACKUP_DIR}" -maxdepth 1 -name "*.gz" -type f -exec ls -lh {} \; 2>/dev/null || echo "  (ninguno)"
else
    echo "✗ Error: El backup falló o el archivo está vacío"
    exit 1
fi

echo ""
echo "¡Proceso de backup finalizado!"
