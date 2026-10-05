package HospitalMS.controller;

import HospitalMS.dto.LoginRequestDTO;
import HospitalMS.dto.LoginResponseDTO;
import HospitalMS.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/login")
    public LoginResponseDTO login(
            @RequestBody LoginRequestDTO dto) {

        String token =
                service.login(dto);

        return new LoginResponseDTO(
                token);
    }

    @GetMapping("/test-role")
    public String testRole(Authentication authentication) {

        return authentication
                .getAuthorities()
                .toString();
    }
}