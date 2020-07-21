#!/bin/bash

# Postgres container entry point
#/bin/bash /usr/local/bin/docker-entrypoint.sh

su - postgres -c "/usr/lib/postgresql/12/bin/postgres -D /var/lib/postgresql/12/main -c config_file=/etc/postgresql/12/main/postgresql.conf"

# Run app
java -jar -Dapplication.logpath="${LOG_PATH}" -Xms512M -Xmx4096M /opt/app/app.jar
