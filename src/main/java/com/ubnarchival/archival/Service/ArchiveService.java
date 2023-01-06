package com.ubnarchival.archival.Service;

import com.ubnarchival.archival.Entity.ArchiveEntity;

import java.util.List;

public interface ArchiveService {

    ArchiveEntity addFile(ArchiveEntity archiveEntity);

    List<ArchiveEntity> getJournals();

    List<ArchiveEntity> getbyTerminal(String terminal, String dates);

    List<ArchiveEntity> getbyTerminalAndDates(String terminal, String dates, String datess);

    List<ArchiveEntity> fetchJournals(String startDate, String endDate, String terminal);

    List<ArchiveEntity> fetchAll();
}
