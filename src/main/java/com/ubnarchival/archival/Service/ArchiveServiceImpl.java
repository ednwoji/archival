package com.ubnarchival.archival.Service;

import com.ubnarchival.archival.Entity.ArchiveEntity;
import com.ubnarchival.archival.Repository.ArchiveRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArchiveServiceImpl implements ArchiveService {

    @Autowired
    ArchiveRepo archiveRepo;
    @Override
    public ArchiveEntity addFile(ArchiveEntity archiveEntity) {
        return archiveRepo.save(archiveEntity);
    }

    @Override
    public List<ArchiveEntity> getJournals() {
        return archiveRepo.findAll();

    }

    @Override
    public ArchiveEntity getbyTerminal(String Terminal, String Dates) {
        return archiveRepo.findByTerminalIDNative(Terminal, Dates);
    }


}
