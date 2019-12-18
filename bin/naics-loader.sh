#!/usr/bin/env bash

cat configuration/$1/tables.csv |
parallel -j 1 -C , "wget {2} -q -O temp.xlsx &&
                    xlsx2csv temp.xlsx       |
                    sed -e '1s/\(.*\)/\L\1/' |
                    sed -e '1s/\s/_/g'       |
                    tr -s ',' ','            |
                    pgfutter --schema warehouse --table $1_{1} csv && 
                    rm temp.xlsx" 