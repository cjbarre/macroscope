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
group by x.series_id;