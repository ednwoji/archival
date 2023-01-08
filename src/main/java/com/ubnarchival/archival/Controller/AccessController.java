package com.ubnarchival.archival.Controller;

import com.ubnarchival.archival.Entity.Branches;
import com.ubnarchival.archival.Entity.Estate;
import com.ubnarchival.archival.Entity.Login;
import com.ubnarchival.archival.Repository.LoginRepo;
import com.ubnarchival.archival.Service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import java.util.List;

@Controller
@RequestMapping("/access")
public class AccessController {

    @Autowired
    LoginService loginService;

    @Autowired
    LoginRepo loginRepo;

    @Value("${backendurl}")
    private String externalService;


    @GetMapping("/user/{pageNo}")
    public String viewUsersPage(@PathVariable(value = "pageNo") int pageNo, Model model) {

        int pageSize = 10;

        Page<Login> page = loginService.FindPaginated(pageNo, pageSize);
        List<Login> listUsers = page.getContent();

        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("listUsers",listUsers);

        return "viewusers";
    }

    @GetMapping("/users")
    public String viewUsers(Model model) {
        return viewUsersPage(1,model);

    }


    @PostMapping("/getUser")
    public String getAtm(@RequestParam("user") String user, RedirectAttributes redirectAttributes){
        Login result = loginService.findUser(user);
        if(result!=null) {
            redirectAttributes.addFlashAttribute("userresult", result);
        }

        else{
            redirectAttributes.addFlashAttribute("record", "No Record Found");
        }
        return "redirect:/access/users";

    }



    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {

        Login login = loginRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User ID: " + id + " not found"));

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Branches>> response = restTemplate.exchange(externalService+"getBranches", HttpMethod.GET, null, new ParameterizedTypeReference<List<Branches>>() {
        });
        List<Branches> responses = response.getBody();



        model.addAttribute("atms", responses);
        model.addAttribute("login", login);

        return "updateuser";
    }



    @PostMapping("/user/{id}")
    public String updateATM(@PathVariable("id") long id, Login login, RedirectAttributes redirectAttributes) {

        return UpdateUser(id, login,redirectAttributes);

    }


    @PutMapping("/updateuser")
    @Modifying
    @Transactional
    public String UpdateUser(long id, Login login, RedirectAttributes redirectAttributes) {

        Login result = loginRepo.findById(id).
                orElseThrow(() -> new IllegalArgumentException("User ID: " + id + " not found"));;

       result.setBranch(login.getBranch());
       result.setUserName(login.getUserName());
       result.setUserRoles(login.getUserRoles());

        loginRepo.save(result);
        redirectAttributes.addFlashAttribute("record", "User Updated Successfully");


        return "redirect:/access/users";


    }








    @PutMapping("/userstatus")
    @Modifying
    @Transactional
    public String MarkInactive(long id, Login login, RedirectAttributes redirectAttributes) {

        Login result = loginRepo.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Estate ID: " + id + " not found"));;

        if(result.getAccess().equals("1")){
            result.setAccess("0");
            redirectAttributes.addFlashAttribute("record", "User Turned Inactive");
        }

        else if(result.getAccess().equals("0")) {
            result.setAccess("1");
            redirectAttributes.addFlashAttribute("record", "User Turned Active");
        }

        loginRepo.save(result);

        return "redirect:/access/users";


    }

    @GetMapping("/inactive/{id}")
    public String changeStatus(@PathVariable("id") long id, Login login, RedirectAttributes redirectAttributes){


        return MarkInactive(id, login, redirectAttributes);

    }





}
