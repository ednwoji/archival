package com.ubnarchival.archival.Repository;

import com.ubnarchival.archival.Entity.Estate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EstateRepository extends JpaRepository<Estate, Long> {

    public Estate findByterminal(String terminal);

    @Query(value = "SELECT COUNT(*) FROM upload WHERE branch = :branch", nativeQuery = true)
    int getNumberofBranches(@Param("branch") String branch);


}
