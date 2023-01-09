package com.ubnarchival.archival.Controller;


import com.google.gson.JsonObject;
import com.ubnarchival.archival.Entity.*;
import com.ubnarchival.archival.Repository.EstateRepository;
import com.ubnarchival.archival.Repository.LoginRepo;
import com.ubnarchival.archival.Repository.UploadErrorRepo;
import com.ubnarchival.archival.Service.ArchiveService;
import com.ubnarchival.archival.Service.EstateService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/fetch")
public class ArchiveController {

    @Autowired
    ArchiveService archiveService;

    @Autowired
    EstateService estateService;

    @Autowired
    EstateRepository estateRepository;

    @Autowired
    UploadErrorRepo uploadErrorRepo;

    @Autowired
    LoginRepo loginRepo;

    @Value("${backendurl}")
    private String externalService;

    @Value("${serverPath}")
    private String backendPath;

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
                              RedirectAttributes redirectAttributes,
                              HttpSession session){


        String checkBranch = (String) session.getAttribute("userBranch");
        String checkRole = (String) session.getAttribute("userRole");
        Estate estate = estateRepository.findByterminal(terminal);
        String currentBranch = estate.getBranch();

        if(!checkRole.equals("BRANCH")) {

            List<ArchiveEntity> journals = (List<ArchiveEntity>) archiveService.fetchJournals(startDate, endDate, terminal);
            System.out.println(journals);
            redirectAttributes.addFlashAttribute("journals", journals);
            return "redirect:/archive/journals";

        }

        else{

            if(!checkBranch.equals(currentBranch)){
                redirectAttributes.addFlashAttribute("error", "You don't have access to view for this Branch");
            }

            else if(checkBranch.equals(currentBranch)){
                List<ArchiveEntity> journals = (List<ArchiveEntity>) archiveService.fetchJournals(startDate, endDate, terminal);
                System.out.println(journals);
                redirectAttributes.addFlashAttribute("journals", journals);
            }
            return "redirect:/archive/journals";
        }

    }


    @GetMapping("/download/{terminal}/{name}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable("terminal") String terminal, @PathVariable("name") String name) throws FileNotFoundException {
        // Retrieve file from provided path
        System.out.println(terminal);
        System.out.println(name);
        Estate result = estateRepository.findByterminal(terminal);
        String branch = result.getBranch();
        File file = new File(backendPath + branch+"\\"+terminal+"\\Journals\\"+name);

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

        RestTemplate restTemplate = new RestTemplate();
        String url = externalService+"addAtm";
        String urls = externalService+"checkIp";

        Map<String, String> requestIp = new HashMap<>();
        requestIp.put("ip", ip);

        String IpResponse = restTemplate.postForObject(urls, requestIp, String.class);

        if(IpResponse.equals("Existing")){

            redirectAttributes.addFlashAttribute("success", "IP Exists on the DB, Use a Free IP");
            return "redirect:/archive/addition";
        }

        else {

            int length = terminalId.length();
            String secondToLastThreeDigits = terminalId.substring(length - 4, length - 1);
            String branchCode = secondToLastThreeDigits + "-" + branch;


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


    @PostMapping("/getAtm")
    public String getAtm(@RequestParam("terminal") String terminal, RedirectAttributes redirectAttributes){
        Estate result = estateService.findAtm(terminal);
        if(result!=null) {
            redirectAttributes.addFlashAttribute("atmresult", result);
        }

        else{
            redirectAttributes.addFlashAttribute("record", "No Record Found");
        }
        return "redirect:/archive/view";

    }

    @PostMapping("/estate/{id}")
    public String updateATM(@PathVariable("id") long id, Estate estate, RedirectAttributes redirectAttributes) {


        int length = estate.getTerminal().length();
        String secondToLastThreeDigits = estate.getTerminal().substring(length - 4, length - 1);
        String branchCode = secondToLastThreeDigits + "-" + estate.getBranch();

        estate.setBranchCode(branchCode);
        estate.setCode(secondToLastThreeDigits);
        estate.setAtmstatus("1");

        estateRepository.save(estate);

        redirectAttributes.addFlashAttribute("record", "Record Successfully Updated");

        return "redirect:/archive/view";

    }

    @PostMapping("/export")
    public void exportToExcel(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
                              HttpServletResponse response) throws IOException {
        // Retrieve data from database

        SXSSFWorkbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        // Retrieve the data from the database
        List<UploadError> data = uploadErrorRepo.findError(startDate, endDate);

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Terminal ID");
        headerRow.createCell(1).setCellValue("Date");
        headerRow.createCell(2).setCellValue("Error Message");
        headerRow.createCell(3).setCellValue("IP Address");

        // Write the data to the sheet
        int rowNum = 1;
        for (UploadError upload : data) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(upload.getTerminalid());
            row.createCell(1).setCellValue(upload.getDate());
            row.createCell(2).setCellValue(upload.getErrorInsert());
            row.createCell(3).setCellValue(upload.getIp());
        }

        // Set the content type and the content disposition
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=UploadStatus.xlsx");

        // Write the workbook to the response
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();


    }


    @PostMapping("/exportuser")
    public void exportUserToExcel(HttpServletResponse response) throws IOException {
        // Retrieve data from database

        SXSSFWorkbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        // Retrieve the data from the database
        List<Login> data = loginRepo.GetAllUsers();

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Username");
        headerRow.createCell(1).setCellValue("Role");
        headerRow.createCell(2).setCellValue("Branch");
        headerRow.createCell(3).setCellValue("Has Access");

        // Write the data to the sheet
        int rowNum = 1;
        for (Login login : data) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(login.getUserName());
            row.createCell(1).setCellValue(login.getUserRoles());
            row.createCell(2).setCellValue(login.getBranch());
            row.createCell(3).setCellValue(login.getAccess());
        }

        // Set the content type and the content disposition
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=UserList.xlsx");

        // Write the workbook to the response
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();


    }

















}
