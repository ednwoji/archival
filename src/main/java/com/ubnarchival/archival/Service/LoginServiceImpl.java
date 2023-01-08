package com.ubnarchival.archival.Service;

import com.ubnarchival.archival.Entity.Estate;
import com.ubnarchival.archival.Entity.Login;
import com.ubnarchival.archival.Repository.EstateRepository;
import com.ubnarchival.archival.Repository.LoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private LoginRepo loginRepo;


    @Override
    public Page<Login> FindPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return loginRepo.findAll(pageable);


    }

    @Override
    public Login findUser(String user) {
        return loginRepo.FetchUsersByName(user);
    }
}
