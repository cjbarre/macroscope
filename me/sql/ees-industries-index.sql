with alphabet_industry as (
    select substr(i.industry_name, 1, 1) as letter,
           i.industry_name,
           i.industry_code
    from warehouse.ees_series s
             join warehouse.ees_industry i
                  on i.industry_code = s.industry_code
    group by 1, 2, 3
)
select ai.letter,
       json_agg(json_build_object('industry_name', ai.industry_name,
                                  'industry_code', ai.industry_code,
                                  'series', (select json_agg(json_build_object('series_id', s.series_id,
                                                                               'series_title', s.series_title,
                                                                               'data_type', dt.data_type_text)
                                                             order by dt.data_type_code, series_title)
                                             from warehouse.ees_series s
                                                      join warehouse.ees_datatype dt
                                                           on dt.data_type_code = s.data_type_code
                                             where s.industry_code = ai.industry_code))
                order by industry_name) as industries
from alphabet_industry ai
group by 1
order by 1;