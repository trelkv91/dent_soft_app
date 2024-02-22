package com.soft.dental.spring.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.soft.dental.spring.models.Clinic;
import com.soft.dental.spring.repository.ClinicRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soft.dental.spring.models.ERole;
import com.soft.dental.spring.models.Role;
import com.soft.dental.spring.models.User;
import com.soft.dental.spring.payload.request.LoginRequest;
import com.soft.dental.spring.payload.request.SignupRequest;
import com.soft.dental.spring.payload.response.JwtResponse;
import com.soft.dental.spring.payload.response.MessageResponse;
import com.soft.dental.spring.repository.RoleRepository;
import com.soft.dental.spring.repository.UserRepository;
import com.soft.dental.spring.security.jwt.JwtUtils;
import com.soft.dental.spring.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Authentikáció", description = "Admin regisztrálhat új felhasználókat; Belépés + token generálódik")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  ClinicRepository clinicRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @Operation(summary = "Bejelentkezés felhasználónév és jelszó párossal", description = "Jogosultság: ADMIN, " +
          "MODERATOR, USER")
  @PostMapping("/signin")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Sikeres bejelentkezés - clinics_id alapján beazonosítható" +
                  ", hogy hova tartozik - active érték: true: engedélyezett, false: törölt", content =
                  {@Content(mediaType =
                  "application/json", schema = @Schema(implementation = JwtResponse.class))}),
          @ApiResponse(responseCode = "401", description = "Hibás adatokkal való belépési kísérlet")
  })
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            userDetails.getClinics_id(),
            userDetails.isActive(),
            roles));
  }

  @Operation(summary = "Regisztráció", description = "Jogosultság: MODERATOR, ADMIN - Admin - 'admin', moderátor - " +
          "'mod' és felhasználó - 'user' jogosultságokat kaphat a regisztált felhasználó.")
  @PostMapping("/signup")
  @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Sikeres regisztráció", content = {@Content(mediaType =
                  "application/json", schema = @Schema(example = "{\"message\": \"User registered successfully!\"}"))}),
          @ApiResponse(responseCode = "400", description = "Hibás kérés")
  })
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
              .badRequest()
              .body(new MessageResponse("Error: Email is already in use!"));
    }

    Clinic clinic = clinicRepository.findById(signUpRequest.getClinics_id()).orElseThrow(() -> new RuntimeException(
            "Clinic is not found"));

    // Create new user's account
    User user = new User(signUpRequest.getUsername(),
            signUpRequest.getEmail(), clinic,
            encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
          case "admin":
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);

            break;
          case "mod":
            Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(modRole);

            break;
          default:
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
}
