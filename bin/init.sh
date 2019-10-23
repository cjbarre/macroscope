#!/usr/bin/env bash

echo "Macroscope initializing."

echo "Waiting for storage to initialize."

sleep 5

echo "Ingesting Establishment Employment Situation"

./bin/bls-loader.sh establishment_employment_situation

# ./bin/bls-loader.sh household_employment_situation

# ./bin/bls-loader.sh job_openings_and_labor_turnover_survey