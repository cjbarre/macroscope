with a_z_index as (
    select substr(nd.indy_text, 1, 1) as letter,
           nd.indy_text as text,
           nd.indy_code as value
    from warehouse.hes_indy nd
    group by 1, 2, 3
)
select ai.letter,
       json_agg(json_build_object('text', ai.text,
                                  'value', ai.value)
                order by ai.value) as items
from a_z_index ai
group by 1
order by 1;