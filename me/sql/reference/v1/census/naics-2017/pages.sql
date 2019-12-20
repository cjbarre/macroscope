with corrected_nulls_01 as (
    select nd1.code as code_01, nd2.code as code_02, nd2.description
    from warehouse.naics_2017_descriptions nd1
             join warehouse.naics_2017_descriptions nd2 on nd1.code || '1' = nd2.code
    where nd1.description = 'NULL'
),
     corrected_nulls_02 as (
         select cn1.code_01, nd.description
         from corrected_nulls_01 cn1
                  join warehouse.naics_2017_descriptions nd on cn1.code_02 || '0' = nd.code
         where cn1.description ilike 'see industry%'
         union
         select cn1.code_01, cn1.description
         from corrected_nulls_01 cn1
         where description not ilike 'see industry%'
     ),
     corrected_pointers as (
         select nd.code as code_01,
                nd2.description
         from warehouse.naics_2017_descriptions nd
                  join
              warehouse.naics_2017_descriptions nd2
              on nd.code || '0' = nd2.code
         where nd.description ilike 'see industry%'
     ),
     naics_2017_descriptions as (
         select *
         from corrected_pointers
         union
         select *
         from corrected_nulls_02
         union
         select code, description
         from warehouse.naics_2017_descriptions
         where description <> 'NULL'
           and description not ilike 'see industry%'
         order by code_01
     )
select code_01 as naics_code,
       regexp_replace(trim(nd2.title),'T$', ' (T)') as title,
       nd.description,
       (select json_agg(cross_reference) from warehouse.naics_2017_industry_cross_references ncr where ncr.code = code_01) as cross_references,
       (select json_agg(index_item_description) from warehouse.naics_2017_index ni where ni.naics17 = code_01) as index_items
from naics_2017_descriptions nd
left join warehouse.naics_2017_descriptions nd2 on nd2.code = code_01
group by 1,2,3
order by code_01;