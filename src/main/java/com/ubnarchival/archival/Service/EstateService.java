package com.ubnarchival.archival.Service;

import com.ubnarchival.archival.Entity.Estate;
import org.springframework.data.domain.Page;

public interface EstateService {

    Page<Estate> FindPaginated(int pageNo, int pageSize);

    Estate findAtm(String terminal);
}
