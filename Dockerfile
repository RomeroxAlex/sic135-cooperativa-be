# Dockerfile para Open Liberty con backup de base de datos PostgreSQL
# Diseñado para funcionar de una vez

FROM icr.io/appcafe/open-liberty:full-java21-openj9-ubi

# Configuración de usuario root para instalación
USER root

# Instalar cliente PostgreSQL para backups
RUN dnf install -y postgresql && dnf clean all

# Configurar zona horaria
ENV TZ=America/El_Salvador

# Variables de entorno para la conexión a la base de datos
ENV DB_HOST=localhost
ENV DB_PORT=5432
ENV DB_USER=postgres
ENV DB_PASSWORD=
ENV DB_NAME=contabilidad
ENV BACKUP_DIR=/backups

# Crear directorio de backups
RUN mkdir -p ${BACKUP_DIR} && chown -R 1001:0 ${BACKUP_DIR}

# Copiar script de backup
COPY --chown=1001:0 backup.sh /opt/ol/backup.sh
RUN chmod +x /opt/ol/backup.sh

# Copiar script de entrada
COPY --chown=1001:0 entrypoint.sh /opt/ol/entrypoint.sh
RUN chmod +x /opt/ol/entrypoint.sh

# Copiar configuración de servidor
COPY --chown=1001:0 src/main/liberty/config/server.xml /config/server.xml

# Definir volumen para persistir los backups
VOLUME ["/backups"]

# Volver a usuario no privilegiado
USER 1001

# Exponer puerto de Open Liberty
EXPOSE 9080 9443

ENTRYPOINT ["/opt/ol/entrypoint.sh"]
