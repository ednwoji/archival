package com.ubnarchival.archival.Entity;


import com.ubnarchival.archival.Enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "logintest")
public class Login {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String UserName;

    @Column(name = "user_password")
    private String UserPassword;

//    @Transient
//    private String token;

    @Column(name = "role")
    private String userRoles;

    @Column(name = "branch")
    private String branch;

    @Column(name = "access")
    private String access;
}
