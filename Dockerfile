# Dockerfile para restaurar automáticamente una base de datos PostgreSQL
# usando un backup en formato custom (pg_dump -Fc)

FROM postgres:16

# Copiar el archivo de backup al contenedor
COPY backup.dump /backup.dump

# Copiar el script de inicialización
COPY init.sh /docker-entrypoint-initdb.d/init.sh

# Hacer el script ejecutable
RUN chmod +x /docker-entrypoint-initdb.d/init.sh
