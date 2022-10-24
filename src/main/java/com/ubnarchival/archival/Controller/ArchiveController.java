package com.ubnarchival.archival.Controller;


import com.ubnarchival.archival.Entity.ArchiveEntity;
import com.ubnarchival.archival.Service.ArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ArchiveController {

    @Autowired
    ArchiveService archiveService;

    @PostMapping("/addfile")
    public ArchiveEntity addNewFile(@Valid @RequestBody ArchiveEntity archiveEntity) {

        return archiveService.addFile(archiveEntity);

    }

    @GetMapping("/find")
    public List<ArchiveEntity> getallFiles() {

        return archiveService.getJournals();

    }


    @GetMapping("/find/{terminal}/{dates}")
    public ArchiveEntity findbyTerminal(@PathVariable("terminal") String Terminal, @PathVariable("dates") String Dates) {

        return archiveService.getbyTerminal(Terminal, Dates);

    }


}
