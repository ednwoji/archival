package com.ubnarchival.archival.Controller;


import com.ubnarchival.archival.Entity.ArchiveEntity;
import com.ubnarchival.archival.Entity.upload;
import com.ubnarchival.archival.Entity.Login;
import com.ubnarchival.archival.Helpers.Token;
import com.ubnarchival.archival.Repository.LoginRepo;
import com.ubnarchival.archival.Service.ArchiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/archive")
public class ArchiveControllerWeb {


    @Autowired
    private LoginRepo loginRepo;

    @Autowired
    private ArchiveService archiveService;

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

        List<ArchiveEntity> journals = (List<ArchiveEntity>) archiveService.fetchAll();
        System.out.println(journals);

//        redirectAttributes.addAttribute("journals", journals);
        modelAndView.addObject("journals", journals);


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
        ResponseEntity<List<upload>> response = restTemplate.exchange("http://localhost:9090/getAll", HttpMethod.GET, null, new ParameterizedTypeReference<List<upload>>() {});
        List<upload> responses = response.getBody();

        model.addAttribute("atms", responses);

        return "addnew";

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



}
