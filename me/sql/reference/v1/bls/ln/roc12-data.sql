with x as (
    select series_id,
           year,
           period,
           value::float                  as current,
           lag(value::float, case
                              when period like 'M%' then 12
                              when period like 'Q%' then 4
                              when period like 'A%' then 1
                             end) over () as previous
    from warehouse.hes_data
    where year::float between date_part('year', now()) - 26 and date_part('year', now())
    and value <> '-'
    --and series_id = 'LNU02036022'
    order by year, period
)
select series_id,
       json_agg(json_build_object('value', case
                                            when previous = 0 then 100
                                            else trunc(cast((current / previous - 1) * 100 as numeric), 3)
                                           end,
                                  'period', case
                                             when period like 'M%'
                                                 then x.year || '-' || regexp_replace(x.period, 'M', '') || '-01 00:00:00'
                                             when period like 'A%'
                                                 then x.year
                                             when period like 'Q%'
                                                 then x.year || '-' || case
                                                                        when period = 'Q01' then '02'
                                                                        when period = 'Q02' then '05'
                                                                        when period = 'Q03' then '08'
                                                                        when period = 'Q04' then '11'
                                                                       end
                                            end)
                order by x.year, x.period) as data
from x
where year::int >= date_part('year', now())::int - 25
group by x.series_id;

