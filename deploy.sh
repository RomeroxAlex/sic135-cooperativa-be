#!/bin/bash
# Script de despliegue para la base de datos PostgreSQL
# Este script clona/actualiza el repositorio, construye la imagen Docker,
# elimina el volumen anterior y levanta el contenedor

set -e

# ============================================
# CONFIGURACIÓN - Modificar según necesidades
# ============================================

# URL del repositorio GitHub (modificar según tu repositorio)
REPO_URL="${REPO_URL:-https://github.com/RomeroxAlex/sic135-cooperativa-be.git}"

# Directorio donde se clonará el repositorio
REPO_DIR="${REPO_DIR:-./sic135-cooperativa-be}"

# Rama del repositorio a usar
REPO_BRANCH="${REPO_BRANCH:-main}"

# Nombre de la imagen Docker
IMAGE_NAME="${IMAGE_NAME:-sic135-postgres}"

# Nombre del contenedor
CONTAINER_NAME="${CONTAINER_NAME:-sic135-postgres-db}"

# Nombre del volumen Docker
VOLUME_NAME="${VOLUME_NAME:-sic135-postgres-data}"

# Configuración de PostgreSQL
POSTGRES_USER="${POSTGRES_USER:-postgres}"
POSTGRES_PASSWORD="${POSTGRES_PASSWORD:-postgres}"
POSTGRES_DB="${POSTGRES_DB:-contabilidad}"

# Puerto donde exponer PostgreSQL
POSTGRES_PORT="${POSTGRES_PORT:-5432}"

# ============================================
# FUNCIONES
# ============================================

echo_step() {
    echo ""
    echo "========================================"
    echo "  $1"
    echo "========================================"
}

# ============================================
# EJECUCIÓN
# ============================================

echo_step "Iniciando despliegue de PostgreSQL"

# Paso 1: Clonar o actualizar el repositorio
echo_step "Paso 1: Clonar/actualizar repositorio"

if [ -d "$REPO_DIR" ]; then
    echo "El repositorio ya existe, actualizando..."
    cd "$REPO_DIR"
    git fetch origin
    git checkout "$REPO_BRANCH"
    git pull origin "$REPO_BRANCH"
else
    echo "Clonando repositorio..."
    git clone -b "$REPO_BRANCH" "$REPO_URL" "$REPO_DIR"
    cd "$REPO_DIR"
fi

# Verificar que backup.dump existe
if [ ! -f "backup.dump" ]; then
    echo "Error: El archivo backup.dump no existe en la raíz del proyecto"
    echo "Por favor, coloque el archivo de backup antes de ejecutar este script"
    exit 1
fi

# Paso 2: Detener y eliminar contenedor existente
echo_step "Paso 2: Detener contenedor existente (si existe)"

if docker ps -a --format '{{.Names}}' | grep -q "^${CONTAINER_NAME}$"; then
    echo "Deteniendo contenedor $CONTAINER_NAME..."
    docker stop "$CONTAINER_NAME" 2>/dev/null || true
    docker rm "$CONTAINER_NAME" 2>/dev/null || true
else
    echo "No hay contenedor existente para eliminar"
fi

# Paso 3: Eliminar volumen anterior para forzar restauración
echo_step "Paso 3: Eliminar volumen anterior"

if docker volume ls --format '{{.Name}}' | grep -q "^${VOLUME_NAME}$"; then
    echo "Eliminando volumen $VOLUME_NAME para forzar restauración..."
    docker volume rm "$VOLUME_NAME"
else
    echo "No hay volumen existente para eliminar"
fi

# Paso 4: Construir la imagen Docker
echo_step "Paso 4: Construir imagen Docker"

docker build -t "$IMAGE_NAME" .

# Paso 5: Crear volumen y levantar contenedor
echo_step "Paso 5: Levantar contenedor"

docker run -d \
    --name "$CONTAINER_NAME" \
    -e POSTGRES_USER="$POSTGRES_USER" \
    -e POSTGRES_PASSWORD="$POSTGRES_PASSWORD" \
    -e POSTGRES_DB="$POSTGRES_DB" \
    -v "${VOLUME_NAME}:/var/lib/postgresql/data" \
    -p "${POSTGRES_PORT}:5432" \
    --restart unless-stopped \
    "$IMAGE_NAME"

# Paso 6: Mostrar estado
echo_step "Despliegue completado"

echo "Contenedor: $CONTAINER_NAME"
echo "Imagen: $IMAGE_NAME"
echo "Volumen: $VOLUME_NAME"
echo "Puerto: $POSTGRES_PORT"
echo ""
echo "Credenciales de conexión:"
echo "  Host: localhost"
echo "  Puerto: $POSTGRES_PORT"
echo "  Usuario: $POSTGRES_USER"
echo "  Contraseña: $POSTGRES_PASSWORD"
echo "  Base de datos: $POSTGRES_DB"
echo ""
echo "Para ver los logs de restauración:"
echo "  docker logs -f $CONTAINER_NAME"
echo ""
echo "Para conectarse a la base de datos:"
echo "  docker exec -it $CONTAINER_NAME psql -U $POSTGRES_USER -d $POSTGRES_DB"
