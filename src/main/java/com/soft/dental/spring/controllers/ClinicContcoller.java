package com.soft.dental.spring.controllers;

import com.soft.dental.spring.models.Clinic;
import com.soft.dental.spring.models.User;
import com.soft.dental.spring.repository.ClinicRepository;
import com.soft.dental.spring.service.ClinicService;
import com.soft.dental.spring.service.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Rendelő", description = "Rendelőkkel kapcsolatos végpontok")
@RestController
@RequestMapping("/api/clinic")
public class ClinicContcoller {

    private ClinicService clinicService;

    @Autowired
    ClinicRepository clinicRepository;

    private final SecurityService securityService;

    public ClinicContcoller (ClinicService theClinicService, SecurityService securityService) {
        clinicService = theClinicService;
        this.securityService = securityService;
    }

    @PostMapping("/add")
    @Operation(summary = "Új rendelő létrehozása", description =  "Jogosultság: ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sikeres rendelő létrehozása",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Clinic.class))}),
    })
    @PreAuthorize("hasRole('ADMIN')")
    public String addClinic(@RequestBody Clinic theClinic){

        theClinic.setId(0);
        clinicRepository.save(theClinic);

        return "Sikeres rendelő létrehozása";
    }

    @GetMapping("/all")

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Összes rendelő lekérdezése", description = "Jogosultság: ADMIN")
    public List<Clinic> findAll() {
        return clinicService.findAll();
    }

    @GetMapping("/current")
    @Operation(summary = "Saját rendelő adatainak lekérdezése", description = "Jogosultság: ADMIN, " +
            "MODERATOR, USER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sikeres lekérdezés", content = {@Content(mediaType =
                    "application/json", schema = @Schema(implementation = Clinic.class))}),
    })
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public Clinic getCurrentClinic(){

        User currentUser = securityService.getCurrentUser();

        Clinic theClinic = clinicService.findById(currentUser.getClinic().getId()).orElse(null);

        return theClinic;
    }

    @GetMapping("/{clinicId}")
    @Operation(summary = "Rendelő adatainak lekérdezése ID alapján", description = "Jogosultság: ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sikeres lekérdezés", content = {@Content(mediaType =
                    "application/json", schema = @Schema(implementation = Clinic.class))}),
    })
    @PreAuthorize("hasRole('ADMIN')")
    public Clinic getClinic(@PathVariable int clinicId){

        Clinic theClinic = clinicService.findById(clinicId).orElse(null);

        if (theClinic == null){
            throw new RuntimeException("Clinic id not found - " + clinicId);
        }

        return theClinic;
    }

     @PatchMapping("/status/{clinicId}/{active}")
     @Operation(summary = "Rendelő aktivitásának frissítése (aktív / nem aktív) ID alapján", description = "Jogosults" +
             "ág: ADMIN")
     @ApiResponses(value = {
             @ApiResponse(responseCode = "200", description = "Sikeres klinaka aktiválás/inaktiválás", content =
                     {@Content(mediaType =
                             "application/json", schema = @Schema(example = "Sikeres klinaka státuszfrissítés."))}),
             @ApiResponse(responseCode = "400", description = "Hibás kérés.")
     })
     @PreAuthorize("hasRole('ADMIN')")
     public String updateStatus(@PathVariable int clinicId, @PathVariable boolean active){

        Clinic theClinic = clinicService.findById(clinicId).orElse(null);
        theClinic.setActive(active);
        clinicRepository.save(theClinic);

         return "Sikeres rendelő státusz frissítés";
     }
}
