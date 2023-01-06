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
    public List<ArchiveEntity> getbyTerminal(String terminal, String dates) {
        return (List<ArchiveEntity>) archiveRepo.findByTerminalAndDatesNative(terminal, dates);
    }

    @Override
    public List<ArchiveEntity> getbyTerminalAndDates(String terminal, String dates, String datess) {
        return (List<ArchiveEntity>) archiveRepo.findByTerminalsAndDatesAndDatesNative(terminal, dates, datess);
    }

    @Override
    public List<ArchiveEntity> fetchJournals(String startDate, String endDate, String terminal) {
        return archiveRepo.fetchJournalsByDates(terminal, startDate,endDate);
    }


}
