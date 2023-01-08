package com.ubnarchival.archival.Controller;


import com.ubnarchival.archival.Entity.*;
import com.ubnarchival.archival.Helpers.Token;
import com.ubnarchival.archival.Repository.EstateRepository;
import com.ubnarchival.archival.Repository.LoginRepo;
import com.ubnarchival.archival.Service.ArchiveService;
import com.ubnarchival.archival.Service.EstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;

@Controller
@RequestMapping("/archive")
public class ArchiveControllerWeb {


    @Autowired
    private LoginRepo loginRepo;

    @Autowired
    private ArchiveService archiveService;

    @Autowired
    private EstateService estateService;

    @Autowired
    private EstateRepository estateRepository;

    @Value("${numberPerPage}")
    private int pageSize;

    @Value("${backendurl}")
    private String externalService;

    @GetMapping("/")
    public ModelAndView homepage(Login login) {

        ModelAndView modelAndView = new ModelAndView("home");
        String token = Token.generateToken();
        modelAndView.addObject("token", token);
        return modelAndView;

    }

    @GetMapping("/dashboard")
    public ModelAndView Dashboard() {

        ModelAndView modelAndView = new ModelAndView("dashboard");

       int count = loginRepo.getTotalUsers();

//        List<ArchiveEntity> journals = (List<ArchiveEntity>) archiveService.fetchAll();
//        System.out.println(journals);

//        redirectAttributes.addAttribute("journals", journals);

        modelAndView.addObject("count", count);


        return modelAndView;

    }

    @GetMapping("/journals")
    public String GetJournals(ArchiveEntity archiveEntity, Model model) {
        return "journal";
    }

    @GetMapping("/footages")
    public String GetFootages(ArchiveEntity archiveEntity, Model model) {

        return "footage";
    }

    @GetMapping("/addition")
    public String createATM(upload atmParam, Model model) {

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<List<Branches>> response = restTemplate.exchange(externalService+"getBranches", HttpMethod.GET, null, new ParameterizedTypeReference<List<Branches>>() {
            });
            List<Branches> responses = response.getBody();
            model.addAttribute("atms", responses);
            return "addnew";

        }
        catch (Exception e) {
            model.addAttribute("atms", "Record Entry Failed. Please try again");
            return "addnew";

        }


    }


    @PostMapping("/login")
    public String login(Login login, Model model, HttpSession session) {

//        String User = login.getUserName();
//        String pass = login.getUserPassword();

        String getPassword = loginRepo.getUserPassword(login.getUserName());

        String getUser = loginRepo.getUsername(login.getUserName());

        String getRole = String.valueOf(loginRepo.getUserRole(login.getUserName()));


        if(getUser != null) {

            if (!BCrypt.checkpw(login.getUserPassword(), getPassword)) {
                model.addAttribute("error", "Incorrect Password");
                return "home";
            }


            else{

                model.addAttribute("name", getUser);

                session.setAttribute("userName", getUser);
                session.setAttribute("userPassword", getPassword);
                session.setAttribute("userRole", getRole);
                System.out.println(getUser);
                System.out.println(getRole);
                return "redirect:/archive/dashboard";
            }

        }

        else {

            model.addAttribute("error", "Incorrect Username");
            return "home";

        }

    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes)
    {
        session.invalidate();
        redirectAttributes.addFlashAttribute("logged_out", "Logged out Successfully");
        return "redirect:/archive/";
    }

    @GetMapping("/dash")
    public String dashboard(HttpSession session, RedirectAttributes redirectAttributes)
    {
        return "redirect:/archive/dashboard";
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


    @GetMapping("/view/{pageNo}")
    public String viewPage(@PathVariable(value = "pageNo") int pageNo, Model model) {


        Page<Estate> page = estateService.FindPaginated(pageNo, pageSize);
        List<Estate> listAtms = page.getContent();

        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("listAtms",listAtms);

        return "viewatm";
    }

    @GetMapping("/view")
    public String viewAtms(Model model) {
       return viewPage(1,model);

    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {

        Estate estate = estateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Estate ID: " + id + " not found"));

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Branches>> response = restTemplate.exchange(externalService+"getBranches", HttpMethod.GET, null, new ParameterizedTypeReference<List<Branches>>() {
        });
        List<Branches> responses = response.getBody();



        model.addAttribute("atms", responses);
        model.addAttribute("estate", estate);

        return "updateatm";
    }

    @PutMapping("/updatestatus")
    @Modifying
    @Transactional
    public String MarkInactive(long id, Estate estate, RedirectAttributes redirectAttributes) {

                Estate result = estateRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Estate ID: " + id + " not found"));;

                if(result.getAtmstatus().equals("1")){
                    result.setAtmstatus("0");
                    redirectAttributes.addFlashAttribute("record", "ATM Turned Inactive");
                }

                else if(result.getAtmstatus().equals("0")) {
                    result.setAtmstatus("1");
                    redirectAttributes.addFlashAttribute("record", "ATM Turned Active");
                }

                estateRepository.save(result);

                return "redirect:/archive/view";


    }

    @GetMapping("/inactive/{id}")
    public String changeStatus(@PathVariable("id") long id, Estate estate, RedirectAttributes redirectAttributes){


        return MarkInactive(id, estate, redirectAttributes);

    }


    @GetMapping("/errors")
    public ModelAndView viewUploadError() {

        ModelAndView modelAndView = new ModelAndView("viewupload");
        return modelAndView;

    }



}
