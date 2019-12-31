select series_id,
       json_agg(json_build_object('value', value,
                                  'period', year || '-' || regexp_replace(period, 'M', '') || '-01T23:00:00Z')) as data
from warehouse.ees_data
where year::int between date_part('year', now()) - 10 and date_part('year', now())
and period <> 'M13'
group by 1;


