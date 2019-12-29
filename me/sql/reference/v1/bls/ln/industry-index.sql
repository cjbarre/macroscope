select i.indy_text                                                                              as title,
       json_agg(json_build_object('value', series_id,
                                  'text', p.periodicity_text || ' ' || series_title)
                order by series_title) as items
from warehouse.hes_series s
         join warehouse.hes_indy i on s.indy_code = i.indy_code
         join warehouse.hes_periodicity p on s.periodicity_code = p.periodicity_code
group by 1;