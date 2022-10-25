package com.ubnarchival.archival.Controller;


import com.ubnarchival.archival.Entity.Login;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ArchiveControllerWeb {


    @GetMapping("/")
    public ModelAndView homepage(Login login) {

        ModelAndView modelAndView = new ModelAndView("home");
        return modelAndView;

    }

    @GetMapping("/dashboard")
    public ModelAndView Dashboard() {

        ModelAndView modelAndView = new ModelAndView("dashboard");
        return modelAndView;

    }

    @PostMapping("/login")
    public String login(Login login) {

        String User = login.getUserName();
        String pass = login.getUserPassword();

        if (User.equals("ednwoji")) {
            return "dashboard";
        }
        else
            return "failure";



    }


}
