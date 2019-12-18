#!/usr/bin/env bash

echo "Macroscope initializing."

echo "Waiting for storage to initialize."

sleep 5

psql -h $DB_HOST -U $DB_USER -d $DB_NAME -f storage/create-anon-role.sql

echo "Ingesting Establishment Employment Situation"

./bin/bls-loader.sh ees

psql -h $DB_HOST -U $DB_USER -d $DB_NAME -f storage/grant-select.sql

echo "Ingesting Household Employment Situation"

./bin/bls-loader.sh hes

psql -h $DB_HOST -U $DB_USER -d $DB_NAME -f storage/grant-select.sql

echo "Ingesting Job Opening and Labor Turnover Survey (JOLTS)"

./bin/bls-loader.sh jolts

psql -h $DB_HOST -U $DB_USER -d $DB_NAME -f storage/grant-select.sql

echo "Ingesting NAICS"

./bin/naics-loader.sh naics_2017

psql -h $DB_HOST -U $DB_USER -d $DB_NAME -f storage/grant-select.sql