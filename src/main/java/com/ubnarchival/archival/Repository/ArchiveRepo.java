package com.ubnarchival.archival.Repository;


import com.ubnarchival.archival.Entity.ArchiveEntity;
import com.ubnarchival.archival.Entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchiveRepo extends JpaRepository<ArchiveEntity, Long> {




    @Query(value = "SELECT * FROM files WHERE Terminal_ID = :terminal AND Date_Added = :dates" , nativeQuery = true)
    List<ArchiveEntity> findByTerminalAndDatesNative(@Param("terminal") String terminal, @Param("dates") String dates);


    @Query(value = "SELECT * FROM files WHERE Terminal_ID = :terminal AND Date_Added BETWEEN :dates AND :datess" , nativeQuery = true)
    List<ArchiveEntity> findByTerminalsAndDatesAndDatesNative(@Param("terminal") String terminal, @Param("dates") String dates, @Param("datess") String date);



}
