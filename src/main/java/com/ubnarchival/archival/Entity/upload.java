package com.ubnarchival.archival.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class upload {


    private Long id;
    private String Branch;
    private String Terminal;
    private String atm;
    private String IP;
    private String code;
    private String branchCode;
    private String state_;
    private String atmStatus;


}
