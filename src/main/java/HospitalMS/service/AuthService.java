package HospitalMS.service;

import HospitalMS.dto.LoginRequestDTO;
import HospitalMS.model.User;
import HospitalMS.repository.UserRepository;
import HospitalMS.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private JwtUtil jwtUtil;

    public String login(
            LoginRequestDTO dto) {

        User user =
                repository.findByUsername(
                                dto.getUsername())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Invalid username"));

        if (!user.getPassword().equals(
                dto.getPassword())) {

            throw new RuntimeException(
                    "Invalid password");
        }

        return jwtUtil.generateToken(user.getUsername(), user.getRole());
    }
}