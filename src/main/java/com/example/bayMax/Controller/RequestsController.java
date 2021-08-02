package com.example.bayMax.Controller;

import com.example.bayMax.Domain.Requests;
import com.example.bayMax.Domain.Roles;
import com.example.bayMax.Domain.Users;
import com.example.bayMax.Infrastructure.RequestsRepository;
import com.example.bayMax.Infrastructure.RolesRepository;
import com.example.bayMax.Infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
public class RequestsController {

    @Autowired
    RolesRepository rolesRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RequestsRepository requestsRepository;


    @GetMapping("/appointment")
    public String appointmentForm(Model model, Principal principal) {
        Users patient = userRepository.findUsersByUsername(principal.getName());
        Roles roles = rolesRepository.findRolesByName("DOCTOR");
        List<Users> doctors = userRepository.findAllByRoles(roles);

        model.addAttribute("doctors", doctors);
        model.addAttribute("patient", patient);
        return "appointment";
    }

    @PostMapping("/appointment")
    public RedirectView selectAppointment(long id, long doctorId) {
        boolean isExist = false;
        Users users = userRepository.findById(id).orElseThrow();
        System.out.println("Doctor name = " + doctorId);
        Users doctor = userRepository.findById(doctorId).orElseThrow();

        for (Requests existingRequest : doctor.getDoctorRequests()) {
            if (existingRequest.getPatient().getId() == id) {
                isExist = true;
                break;
            }
        }
        if (!isExist) {
            Requests request = new Requests();
            request.setDoctor(doctor);
            request.setPatient(users);
            request.setAccepted(false);
            requestsRepository.save(request);

        } else {
            System.out.println("Request is found");
            for (Requests request: users.getPatientRequests()
                 ) {
                System.out.println(request.getDoctor().getFullName());
            }
//            System.out.println(doctor.getDoctorRequests().toArray().toString());
//            System.out.println(users.getPatientRequests().toArray().toString());
        }
        return new RedirectView("/appointment");
    }

    @GetMapping("/requests")
    public String getRequests(Principal principal, Model model) {
        Users doctor = userRepository.findUsersByUsername(principal.getName());
        Set<Requests> requests = doctor.getDoctorRequests();
        model.addAttribute("requests", requests);
        for (Requests request:doctor.getDoctorRequests()
             ) {
            System.out.println(request.getDoctor().getFullName());
        }

        return "requests";
    }

    @PostMapping("/requests")
    public RedirectView acceptRequests(Principal principal, long id, boolean accepted, long requestId) {
        Users patient = userRepository.findById(id).orElseThrow();
        Users doctor = userRepository.findUsersByUsername(principal.getName());
        Requests request = requestsRepository.findById(requestId).orElseThrow();
        if (accepted) {
            request.setAccepted(true);
            requestsRepository.save(request);
            patient.getPatientRequests().add(request);
        } else {
            requestsRepository.delete(request);
            doctor.getDoctorRequests().remove(request);
        }
        return new RedirectView("/requests");
    }
}
