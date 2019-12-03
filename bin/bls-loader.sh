#!/usr/bin/env bash

cat configuration/$1/tables.csv |
parallel -C , "wget {2} -q -O - |
               tsv2csv |
               pgfutter --schema warehouse --table $1_{1} csv"