package com.ubnarchival.archival.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "upload")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Estate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "branch")
    private String branch;

    @Column(name = "terminal")
    private String terminal;

    @Column(name = "atm")
    private String atm;

    @Column(name = "ip")
    private String ip;

    @Column(name = "code")
    private String code;

    @Column(name = "branchcode")
    private String branchCode;

    @Column(name = "state")
    private String state;

    @Column(name = "atmstatus")
    private String atmstatus;

}
