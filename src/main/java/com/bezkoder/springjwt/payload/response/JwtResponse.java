package com.bezkoder.springjwt.payload.response;

import lombok.Data;
import java.util.List;

@Data
public class JwtResponse {
  private Integer id;
  private String username;
  private String email;
  private Integer clinics_id;
  private List<String> roles;
  private String token;
  private String type = "Bearer";

  public JwtResponse(String accessToken, Integer id, String username, String email, Integer
                     clinics_id, List<String> roles) {
    this.token = accessToken;
    this.id = id;
    this.username = username;
    this.email = email;
    this.clinics_id = clinics_id;
    this.roles = roles;
  }
}
