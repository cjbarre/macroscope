#!/usr/bin/env bash

echo "Macroscope initializing."

echo "Waiting for storage to initialize."

sleep 5

echo "Ingesting Establishment Employment Situation"

./bin/bls-loader.sh establishment_employment_situation

echo "Ingesting Household Employment Situation"

./bin/bls-loader.sh household_employment_situation

echo "Ingesting Job Opening and Labor Turnover Survey (JOLTS)"

./bin/bls-loader.sh jolts