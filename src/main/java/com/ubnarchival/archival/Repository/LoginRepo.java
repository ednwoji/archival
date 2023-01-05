package com.ubnarchival.archival.Repository;

import com.ubnarchival.archival.Entity.ArchiveEntity;
import com.ubnarchival.archival.Entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepo extends JpaRepository<Login, Long> {

    @Query(value = "SELECT user_password FROM loginTest WHERE user_name = :user_name", nativeQuery = true)
    String getUserPassword(@Param("user_name") String userName);

    @Query(value = "SELECT role FROM loginTest WHERE user_name = :user_name", nativeQuery = true)
    String getUserRole(@Param("user_name") String userName);

    @Query(value = "SELECT * FROM loginTest WHERE user_name = :user_name", nativeQuery = true)
    Login getUsers(@Param("user_name") String userName);

    @Query(value = "SELECT user_name FROM loginTest WHERE user_name = :user_name", nativeQuery = true)
    String getUsername(@Param("user_name") String userName);




}
