package com.ubnarchival.archival.Service;

import com.ubnarchival.archival.Entity.ArchiveEntity;

import java.util.List;

public interface ArchiveService {

    ArchiveEntity addFile(ArchiveEntity archiveEntity);

    List<ArchiveEntity> getJournals();

    ArchiveEntity getbyTerminal(String Terminal, String Dates);
}
