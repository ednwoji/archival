package com.ubnarchival.archival.Controller;


import com.google.gson.JsonObject;
import com.ubnarchival.archival.Entity.ArchiveEntity;
import com.ubnarchival.archival.Entity.upload;
import com.ubnarchival.archival.Service.ArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/getjournals")
    public String GetJournals(@RequestParam("terminal") String terminal,
                              @RequestParam("startDate") String startDate,
                              @RequestParam("endDate") String endDate,
                              RedirectAttributes redirectAttributes){


        List<ArchiveEntity> journals = (List<ArchiveEntity>)archiveService.fetchJournals(startDate, endDate, terminal);
        System.out.println(journals);
        redirectAttributes.addFlashAttribute("journals", journals);
        return "redirect:/archive/journals";


    }


    @GetMapping("/download/{name}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String name) throws FileNotFoundException {
        // Retrieve file from provided path
        File file = new File("C:\\Users\\ednwoji\\Documents\\" + name);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        // Set response headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", String.format("attachment; filename=%s", file.getName()));

        // Return response with file as body and headers
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    @PostMapping("/saveAtm")
    public String addAtm(@RequestParam("atm") String atm,
                         @RequestParam("ip") String ip,
                         @RequestParam("terminal") String terminalId,
                         @RequestParam("branch") String branch,
                         @RequestParam("state") String state,
                         RedirectAttributes redirectAttributes) {

        JsonObject jsonObject = new JsonObject();
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:9090/addAtm";

        int length = terminalId.length();
        String secondToLastThreeDigits = terminalId.substring(length - 4, length - 1);
        String branchCode = secondToLastThreeDigits+"-"+branch;

//        jsonObject.addProperty("terminal", terminalId);
//        jsonObject.addProperty("atm", atm);
//        jsonObject.addProperty("branch", branch);
//        jsonObject.addProperty("ip", ip);
//        jsonObject.addProperty("branchCode", branchCode);
//        jsonObject.addProperty("code", secondToLastThreeDigits);
//        jsonObject.addProperty("state_", state);
//
//        HttpEntity<?> entity = new HttpEntity<>(jsonObject.toString(), null);
//        upload response = restTemplate.postForObject(url, entity, upload.class);
//        System.out.println(response);


        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("terminal", terminalId);
        requestBody.put("atm", atm);
        requestBody.put("branch", branch);
        requestBody.put("ip", ip);
        requestBody.put("branchCode", branchCode);
        requestBody.put("code", secondToLastThreeDigits);
        requestBody.put("state_", state);

        System.out.println(secondToLastThreeDigits);

// Make the POST request
        upload response = restTemplate.postForObject(url, requestBody, upload.class);
        System.out.println(response);

        redirectAttributes.addFlashAttribute("success", "Record Successfully Saved");

        return "redirect:/archive/addition";

    }








}
