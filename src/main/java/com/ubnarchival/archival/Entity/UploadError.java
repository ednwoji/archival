package com.ubnarchival.archival.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "error")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UploadError {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "terminal_id")
    private String terminalid;

    @Column(name = "ip")
    private String ip;

    @Column(name = "error_date")
    private String date;

    @Column(name = "error_insert")
    private String errorInsert;

}
