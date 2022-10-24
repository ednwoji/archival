package com.ubnarchival.archival.Repository;


import com.ubnarchival.archival.Entity.ArchiveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArchiveRepo extends JpaRepository<ArchiveEntity, Long> {



    @Query(value = "SELECT * FROM files WHERE Date_Added = :dates AND Terminal_ID = :terminal", nativeQuery = true)
    ArchiveEntity findByTerminalIDNative(@Param("dates") String dates, @Param("terminal") String terminal);

}
