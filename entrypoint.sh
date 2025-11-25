#!/bin/bash

# Script de entrada para Docker
# Ejecuta backup de base de datos y luego inicia Open Liberty

set -e

echo "============================================"
echo "  Iniciando contenedor Open Liberty"
echo "============================================"

# Ejecutar backup si las variables de BD están configuradas
if [ -n "${DB_HOST}" ] && [ -n "${DB_NAME}" ]; then
    echo "Ejecutando backup de base de datos..."
    /opt/ol/backup.sh || echo "Advertencia: No se pudo realizar el backup"
    echo ""
fi

echo "Iniciando servidor Open Liberty..."
echo "============================================"

# Ejecutar Open Liberty
exec /opt/ol/wlp/bin/server run defaultServer
