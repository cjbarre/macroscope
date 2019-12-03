select series_id,
       json_agg(json_build_object('series_id', series_id,
                                  'value', value,
                                  'period', year || '-' || regexp_replace(period, 'M', ''))) as data
from warehouse.ees_data
where year::int between date_part('year', now()) - 10 and date_part('year', now())
group by 1;