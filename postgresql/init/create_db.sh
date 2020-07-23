#!/bin/bash

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL

	    CREATE USER user_auth PASSWORD 'test';
	    CREATE DATABASE service_auth OWNER user_auth;

	    \connect service_auth
      CREATE SCHEMA IF NOT EXISTS user_info AUTHORIZATION user_auth

EOSQL
