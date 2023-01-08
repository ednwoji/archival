package com.ubnarchival.archival.Service;

import com.ubnarchival.archival.Entity.Login;
import org.springframework.data.domain.Page;

public interface LoginService {

    Page<Login> FindPaginated(int pageNo, int pageSize);

    Login findUser(String user);
}
