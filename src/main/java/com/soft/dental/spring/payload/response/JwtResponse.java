package com.soft.dental.spring.payload.response;

import lombok.Data;
import java.util.List;

@Data
public class JwtResponse {
  private Integer id;
  private String username;
  private String email;
  private Integer clinics_id;
  private boolean active;
  private List<String> roles;
  private String token;
  private String type = "Bearer";

  public JwtResponse(String accessToken, Integer id, String username, String email, Integer
                     clinics_id, boolean active, List<String> roles) {
    this.token = accessToken;
    this.id = id;
    this.username = username;
    this.email = email;
    this.clinics_id = clinics_id;
    this.active = active;
    this.roles = roles;
  }
}
