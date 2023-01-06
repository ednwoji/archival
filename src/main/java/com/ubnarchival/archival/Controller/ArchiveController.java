package com.ubnarchival.archival.Controller;


import com.ubnarchival.archival.Entity.ArchiveEntity;
import com.ubnarchival.archival.Service.ArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/fetch")
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
    public List<ArchiveEntity> findbyTerminal(@PathVariable("terminal") String terminal, @PathVariable("dates") String dates) {

        return archiveService.getbyTerminal(terminal, dates);

    }

    @GetMapping("/search/{terminal}/{dates}/{datess}")
    public List<ArchiveEntity> findbyDates(@PathVariable("terminal") String terminal,
                                     @PathVariable("dates") String dates,
                                     @PathVariable("datess") String datess) {

        return archiveService.getbyTerminalAndDates(terminal, dates, datess);

    }

    @PostMapping(path = "/getjournals", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
    public String GetJournals(@RequestParam("terminal") String terminal,
                              @RequestParam("startDate") String startDate,
                              @RequestParam("endDate") String endDate,
                              RedirectAttributes redirectAttributes){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json;charset=UTF-8;");

        System.out.println(startDate);
        System.out.println(endDate);

        List<ArchiveEntity> journals = archiveService.fetchJournals(startDate, endDate, terminal);
        System.out.println(journals);
        redirectAttributes.addAttribute("journalFiles", journals);

        return "redirect:/journals";

    }



}
