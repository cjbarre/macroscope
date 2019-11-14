select series_id,
       json_agg(json_build_object('series_id', series_id,
                                  'value', value,
                                  'period', year || '-' || regexp_replace(period, 'M', ''))) as data
from warehouse.ees_data
group by 1;