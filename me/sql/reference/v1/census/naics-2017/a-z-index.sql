with a_z_index as (
    select substr(nd.code, 1, 1) as letter,
           regexp_replace(trim(nd.title), 'T$', ' (T)') as text,
           nd.code as value
    from warehouse.naics_2017_descriptions nd
    group by 1, 2, 3
)
select ai.letter,
       json_agg(json_build_object('text', ai.text,
                                  'value', ai.value)
                order by ai.value) as items
from a_z_index ai
group by 1
order by 1;