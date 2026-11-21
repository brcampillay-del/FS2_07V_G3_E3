package cl.duoc.levelupgamer.controller;

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
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest input) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(input.email(), input.password())
            );

            String rol = auth.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");

            Instant now = Instant.now();
            long expiresIn = 900;

            JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("levelupgamer-api")
                .subject(input.email())
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
                "email", input.email(),
                "rol", rol
            ));

        } catch (AuthenticationException ex) {
            return ResponseEntity.status(401).body(Map.of("message", "Credenciales incorrectas"));
        }
    }
}
