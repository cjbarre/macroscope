select s.*, s.periodicity_code as period_type, i.indy_text
from warehouse.hes_series s
         join warehouse.hes_indy i on i.indy_code = s.indy_code
         where series_id = 'LNU02036022';