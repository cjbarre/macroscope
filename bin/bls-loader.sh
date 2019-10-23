#!/usr/bin/env bash

cat configuration/$1/tables.csv |
parallel -C , "wget {2} -q -O - |
               tsv2csv |
               pgfutter --schema $1 --table {1} csv"