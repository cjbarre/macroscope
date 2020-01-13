with x as (
    select series_id,
        year,
        period,
        value::float                  as current,
        lag(value::float, 12) over (order by series_id, year, period) as previous
    from warehouse.ees_data
    where year::int between 2018 and 2019
    and period <> 'M13'
)
select series_id,
    json_agg(json_build_object('value', trunc(cast((current / previous - 1) * 100 as numeric), 3),
                                'period', year || '-' || regexp_replace(period, 'M', '') || '-01T23:00:00Z')
                order by x.year, x.period) as data
from x
where year = '2019'
group by x.series_id;