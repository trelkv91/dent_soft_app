package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.models.Clinic;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.service.ClinicService;
import com.bezkoder.springjwt.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Rendelő", description = "Rendelőkkel kapcsolatos végpontok")
@RestController
@RequestMapping("/api/clinic")
public class ClinicContcoller {

    private ClinicService clinicService;

    @Autowired
    public ClinicContcoller (ClinicService theClinicService) {
        clinicService = theClinicService;
    }

    @GetMapping("/{clinicId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public Clinic getClinic(@PathVariable int clinicId){

        Clinic theClinic = clinicService.findById(clinicId);

        if (theClinic == null){
            throw new RuntimeException("Clinic id not found - " + clinicId);
        }

        return theClinic;
    }
}
