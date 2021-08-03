package com.example.bayMax.Controller;

import com.example.bayMax.Domain.Record;
import com.example.bayMax.Domain.Requests;
import com.example.bayMax.Domain.Roles;
import com.example.bayMax.Domain.Users;
import com.example.bayMax.Infrastructure.RecodRepository;
import com.example.bayMax.Infrastructure.RequestsRepository;
import com.example.bayMax.Infrastructure.UserRepository;
import org.apache.catalina.User;
import org.apache.coyote.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class recordController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RecodRepository recodRepository;

    @Autowired
    RequestsRepository requestsRepository;

    @GetMapping("/records/{id}")
    String getRecords(@PathVariable("id") Long id, Model m) {
        Users patient = userRepository.findById(id).get();
        patient = userRepository.save(patient);
        m.addAttribute("user", patient);
        Record addRecord = new Record("asdas", "sdfsdf", patient);

        return ("record");
    }


    @PostMapping("/addrecord/{id}")
    RedirectView postRecord(@RequestParam(value = "drugs") String drugs, @RequestParam(value = "diagnostic") String diagnostic, @PathVariable("id") Long id) {
        Users patient = userRepository.findById(id).get();

        Record newRecord = new Record(drugs, diagnostic, patient);
        newRecord = recodRepository.save(newRecord);

        return new RedirectView("/");

    }

    @GetMapping("/patients")
    public String getPatients(Model m, Principal p){

//        UserDetails userDetails= (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Users currentUser= userRepository.findUsersByUsername(p.getName());
        System.out.println("nnnnnnnnnnnnnnn"+currentUser.getUsername());
        List<Requests> request = requestsRepository.findAllByDoctorAndIsAccepted(currentUser,true);
        List<Users> patients= new ArrayList<>();

        System.out.println("rrrrrrrrrrrrrrrrreeeeeeeeeeqqqqqqqqq"+request.toArray().toString());

        request.forEach((element)->{patients.add(element.getPatient());});
        m.addAttribute("patients",patients);
        System.out.println("rrrrrrrrrrrrrrrrreeeeeeeeeeqqqqqqqqq"+patients.toArray().toString());

        return "patients.html";
    }



}
