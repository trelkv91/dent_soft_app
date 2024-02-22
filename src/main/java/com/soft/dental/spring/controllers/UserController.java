package com.soft.dental.spring.controllers;

import com.soft.dental.spring.models.User;
import com.soft.dental.spring.repository.UserRepository;
import com.soft.dental.spring.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Felhasználó", description = "Felhasználóval kapcsolatos végpontok")
@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public UserController(UserService theUserService) {
        userService = theUserService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Összes felhasználó lekérdezése", description = "Jogosultság: ADMIN")
    public List<User> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{user_id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Felhasználó adatainak lekérdezése ID alapján", description = "Jogosultság: ADMIN")
    public User findUser(@PathVariable int user_id) {
        return userService.findById(user_id).orElse(null);
    }

    @PutMapping("/{user_id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Felhasználó adatainak frissítése", description = "Frissíthető adatok: username, email, " +
            "first_name, last_name, active - Jogosultság: ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sikeres felhasználói törzsadatok frissítése",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))}),
    })
    public ResponseEntity<User> updateUser(@PathVariable int user_id, @RequestBody User userDetails) {
        User updateUser = userRepository.findById(user_id)
                .orElseThrow(() -> new RuntimeException(
                        "User is not found"));

        updateUser.setUsername(userDetails.getUsername());
        updateUser.setEmail(userDetails.getEmail());
        updateUser.setFirst_name(userDetails.getFirst_name());
        updateUser.setLast_name(userDetails.getLast_name());
        updateUser.setActive(userDetails.isActive());

        userRepository.save(updateUser);
        return ResponseEntity.ok(updateUser);
    }

    @PatchMapping("/active/{userId}/{active}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Felhasználó aktiválása / inaktiválása", description = "\"TÖrlés\" - Jogosultság: ADMIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sikeres felhasználó aktiválás/inaktiválás", content =
                    {@Content(mediaType =
                    "application/json", schema = @Schema(example = "Sikeres felhasználói státuszfrissítés."))}),
            @ApiResponse(responseCode = "400", description = "Hibás kérés.")
    })
    public String updateUserStatus(@PathVariable int userId, @PathVariable boolean active) {

        User theUser = userService.findById(userId).orElse(null);
        theUser.setActive(active);
        userRepository.save(theUser);

        return "Sikeres felhasználói státusz frissítés. Felhasználó státusza: " + active;
    }

}
