#!/bin/bash

cd "$(dirname "$0")" # set working directory to script directory
set -o allexport; source ../docker.env; set +o allexport

docker run --rm -it -v $(pwd)/changelog:/liquibase/changelog --network messenger-net \
 liquibase/liquibase --url="${DB_URL}" --username="${DB_USER}" --password="${DB_PASS}" \
 --changeLogFile=changelog/db.changelog-master.yaml $1 # e.g. update
