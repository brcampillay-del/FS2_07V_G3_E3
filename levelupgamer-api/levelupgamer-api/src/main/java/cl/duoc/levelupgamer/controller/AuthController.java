package cl.duoc.levelupgamer.controller;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import cl.duoc.levelupgamer.dto.LoginRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final InMemoryUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtEncoder jwtEncoder,
            InMemoryUserDetailsManager userDetailsManager,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest input) {
        String email = input.email().trim().toLowerCase();

        if (!userDetailsManager.userExists(email)) {
            return ResponseEntity.status(401)
                .body(Map.of("message", "La cuenta no está registrada. Por favor, regístrate primero."));
        }

        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, input.password())
            );

            String rol = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

            Instant now = Instant.now();
            long expiresIn = 900;

            JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("levelupgamer-api")
                .subject(email)
                .claim("rol", rol)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .build();

            JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
            String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

            return ResponseEntity.ok(Map.of(
                "accessToken", token,
                "tokenType", "Bearer",
                "expiresIn", expiresIn,
                "email", email,
                "rol", rol
            ));

        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body(Map.of("message", "Contraseña incorrecta. Inténtalo nuevamente."));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {
        String nombre = request.get("nombre");
        String email = request.get("email");
        String password = request.get("password");

        if (nombre == null || nombre.isBlank() ||
            email == null || email.isBlank() ||
            password == null || password.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Todos los campos son obligatorios."));
        }

        String emailNormalizado = email.trim().toLowerCase();

        if (userDetailsManager.userExists(emailNormalizado)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Este correo ya está asociado a una cuenta."));
        }

        UserDetails nuevoUsuario = User.builder()
                .username(emailNormalizado)
                .password(passwordEncoder.encode(password))
                .roles("CLIENTE")
                .build();

        userDetailsManager.createUser(nuevoUsuario);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "message", "Usuario registrado correctamente.",
                        "email", emailNormalizado,
                        "rol", "CLIENTE"
                ));
    }
}
