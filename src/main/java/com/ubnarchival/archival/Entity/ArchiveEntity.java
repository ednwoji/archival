package com.ubnarchival.archival.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "files")
public class ArchiveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "Name_")
    private String name;

    @Column(name = "Terminal_ID")
    private String terminal;

    @Column(name = "Date_Added")
    private String startDate;

    @Transient
    private String endDate;
}
