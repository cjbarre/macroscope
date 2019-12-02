with primary_data as (
    select series_id,
           json_agg(json_build_object('value', value,
                                      'period', year || '-' || regexp_replace(period, 'M', ''))) as data
    from warehouse.ees_data
    where year::int between date_part('year', now()) - 10 and date_part('year', now())
    group by 1
),
     rate_of_change_12_mo as (
         with x as (
             select series_id,
                    year,
                    period,
                    value::float                  as current,
                    lag(value::float, 12) over () as previous
             from warehouse.ees_data
             where year::float between date_part('year', now()) - 1 and date_part('year', now())
             order by year, period
         )
         select series_id,
                json_agg(json_build_object('value', trunc(cast((current / previous - 1) * 100 as numeric), 3),
                                           'period', year || '-' || regexp_replace(period, 'M', ''))
                         order by x.year, x.period) as data
         from x
         where year = date_part('year', now())::text
         group by x.series_id
     )
select series_id,
       json_build_object('series_id', d.series_id,
                         'primary_data', (select data from primary_data pd where d.series_id = pd.series_id),
                         'rate_of_change_12_mo', (select data from rate_of_change_12_mo roc where d.series_id = roc.series_id)) as data
from warehouse.ees_data d
where series_id = 'CES0500000001'
group by 1;