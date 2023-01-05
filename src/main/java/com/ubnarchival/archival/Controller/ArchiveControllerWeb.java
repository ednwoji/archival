package com.ubnarchival.archival.Controller;


import ch.qos.logback.classic.pattern.SyslogStartConverter;
import com.ubnarchival.archival.Entity.Login;
import com.ubnarchival.archival.Helpers.Token;
import com.ubnarchival.archival.Repository.ArchiveRepo;
import com.ubnarchival.archival.Repository.LoginRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ArchiveControllerWeb {


    @Autowired
    private LoginRepo loginRepo;

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
        return modelAndView;

    }

    @GetMapping("/journals")
    public ModelAndView GetJournals() {

        ModelAndView modelAndView = new ModelAndView("journal");
        return modelAndView;

    }

    @GetMapping("/footages")
    public ModelAndView GetFootages() {

        ModelAndView modelAndView = new ModelAndView("footage");
        return modelAndView;

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
                return "redirect:/dashboard";
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
        return "redirect:/";
    }

    @GetMapping("/dash")
    public String dashboard(HttpSession session, RedirectAttributes redirectAttributes)
    {
        return "redirect:/dashboard";
    }


}
