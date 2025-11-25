# Dockerfile para backup de base de datos MySQL/MariaDB
# Diseñado para funcionar de una vez con backup automático

FROM alpine:3.21

# Instalar cliente MySQL y herramientas necesarias
RUN apk add --no-cache \
    mysql-client \
    bash \
    gzip \
    tzdata

# Configurar zona horaria (por defecto America/El_Salvador)
ENV TZ=America/El_Salvador

# Variables de entorno para la conexión a la base de datos
ENV DB_HOST=localhost
ENV DB_PORT=3306
ENV DB_USER=root
ENV DB_PASSWORD=
ENV DB_NAME=contabilidad
ENV BACKUP_DIR=/backups

# Crear directorio de backups
RUN mkdir -p ${BACKUP_DIR}

# Copiar script de backup
COPY backup.sh /usr/local/bin/backup.sh

# Dar permisos de ejecución al script
RUN chmod +x /usr/local/bin/backup.sh

# Definir volumen para persistir los backups
VOLUME ["/backups"]

# Punto de entrada
ENTRYPOINT ["/usr/local/bin/backup.sh"]
