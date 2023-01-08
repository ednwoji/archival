package com.ubnarchival.archival.Service;


import com.ubnarchival.archival.Entity.Estate;
import com.ubnarchival.archival.Repository.EstateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class EstateServiceImple implements EstateService {
    @Autowired
    private EstateRepository estateRepository;
    @Override
    public Page<Estate> FindPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return estateRepository.findAll(pageable);
    }

    @Override
    public Estate findAtm(String terminal) {
        return estateRepository.findByterminal(terminal);
    }
}
