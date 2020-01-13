select i.industry_name || ' index' as industry, 
       json_agg(lower(s.series_id) || ' ' || s.series_title) as items
from warehouse.ees_industry i
         join warehouse.ees_series s on s.industry_code = i.industry_code
group by i.industry_name;