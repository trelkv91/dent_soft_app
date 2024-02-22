package com.soft.dental.spring.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", 
    uniqueConstraints = { 
      @UniqueConstraint(columnNames = "username"),
      @UniqueConstraint(columnNames = "email") 
    })
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @NotBlank
  @Size(max = 20)
  private String username;

  @NotBlank
  @Size(max = 120)
  private String password;

  @Size(max = 255)
  @Email
  private String email;

  private Date last_login;

  @Size(max = 100)
  private String first_name;

  @Size(max = 100)
  private String last_name;

  @Column(nullable = false, columnDefinition = "TINYINT(1)")
  private boolean active;

  @ManyToOne
  @JoinColumn(name="clinics_id")
  private Clinic clinic;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(  name = "user_roles",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  public User(String username, String email, Clinic clinic, String password) {
    this.username = username;
    this.email = email;
    this.clinic = clinic;
    this.password = password;
  }
}
