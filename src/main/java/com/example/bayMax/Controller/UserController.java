package com.example.bayMax.Controller;

import com.example.bayMax.Domain.Roles;
import com.example.bayMax.Domain.Users;
import com.example.bayMax.Infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;
import java.sql.Date;
import java.util.ArrayList;



@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/")
    public String homePage(){
        return "home";
    }
    @GetMapping("/signup")
    public String getSignupPage(){
        return "signup";
    }

    @GetMapping("/login")
    public String getLoginPge(){

        return "login";
    }

    @GetMapping("/myprofile")
    public String getProfilePage(Model model){
        UserDetails userDetails= (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        Users users = userRepository.findUsersByUsername(userDetails.getUsername());
        model.addAttribute("username",userDetails.getUsername());

        model.addAttribute("userInfo",users);
        return "profile";
    }

    // Add new users with user (patient) role
    @PostMapping("/signup")
    public RedirectView trySignUp(@RequestParam String firstname,
                                  @RequestParam String lastname,
                                  @RequestParam String username,
                                  @RequestParam String password,
                                  @RequestParam String location,
                                  @RequestParam String bloodType,
                                  @RequestParam Date dateOfBirth,
                                  @RequestParam Long nationalId){
        Users newUser = new Users(firstname,lastname,dateOfBirth,location,bloodType,nationalId,username,bCryptPasswordEncoder.encode(password));
        newUser.addRole(new Roles("USER"));
        newUser = userRepository.save(newUser);

        UsernamePasswordAuthenticationToken authentication= new UsernamePasswordAuthenticationToken(newUser,null,new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new RedirectView("/");
    }

//    @GetMapping("/access-denied")
//    public String getAccessDenied() {
//        return "error";
//    }


    // Add new users with doctor role
    @PostMapping("/doctors")
    public RedirectView addDoctors(@RequestParam String firstname,
                                   @RequestParam String lastname,
                                   @RequestParam String username,
                                   @RequestParam String password,
                                   @RequestParam String location,
                                   @RequestParam String bloodType,
                                   @RequestParam Date dateOfBirth,
                                   @RequestParam Long nationalId){
        Users newUser = new Users(firstname,lastname,dateOfBirth,location,bloodType,nationalId,username,bCryptPasswordEncoder.encode(password));
        newUser.addRole(new Roles("DOCTOR"));
        newUser = userRepository.save(newUser);
        return new RedirectView("/");
    }

}
