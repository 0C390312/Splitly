#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL

     CREATE USER ddl_user WITH PASSWORD '$DDL_PASSWORD';
     CREATE USER dml_user WITH PASSWORD '$DML_PASSWORD';

     CREATE SCHEMA splitly_app;

     GRANT CONNECT ON DATABASE splitly_db TO ddl_user;
     GRANT USAGE, CREATE ON SCHEMA splitly_app TO ddl_user;

     GRANT CONNECT ON DATABASE splitly_db TO dml_user;
     GRANT USAGE, SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA splitly_app TO dml_user;
EOSQL
