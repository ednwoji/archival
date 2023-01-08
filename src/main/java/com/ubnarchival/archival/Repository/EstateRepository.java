package com.ubnarchival.archival.Repository;

import com.ubnarchival.archival.Entity.Estate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstateRepository extends JpaRepository<Estate, Long> {

    public Estate findByterminal(String terminal);


}
