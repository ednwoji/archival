package com.ubnarchival.archival.Repository;

import com.ubnarchival.archival.Entity.UploadError;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UploadErrorRepo extends JpaRepository<UploadError, Long> {


    @Query(value = "SELECT * FROM error WHERE error_date BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<UploadError> findError(@Param("startDate") String startDate,
                                @Param("endDate") String endDate);

}
