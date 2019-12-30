select d.series_id,
       json_agg(json_build_object('value', d.value,
                                  'period', case
                                             when d.period like 'M%'
                                                 then d.year || '-' || regexp_replace(d.period, 'M', '') || '-01 00:00:00'
                                             when d.period like 'A%'
                                                 then d.year
                                             when d.period like 'Q%'
                                                 then d.year || '-' || case
                                                                        when d.period = 'Q01' then '02'
                                                                        when d.period = 'Q02' then '05'
                                                                        when d.period = 'Q03' then '08'
                                                                        when d.period = 'Q04' then '11'
                                                                       end
                                            end)) as data
from warehouse.hes_data d
where d.year::int between date_part('year', now()) - 25 and date_part('year', now())
--and series_id = 'LNU02036022'
group by 1;