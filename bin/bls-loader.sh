#!/usr/bin/env bash

cat configuration/$1/tables.csv |
parallel -C , "wget {2} -q -O - |
               tsv2csv |
               pgfutter --schema warehouse --table $1_{1} csv"


ls | head -n 10 |
parallel "vl2vg {1} | vg2svg | sed 's/width=\"[0-9]*\" height=\"[0-9]*\"//' > svg/{1}.svg"