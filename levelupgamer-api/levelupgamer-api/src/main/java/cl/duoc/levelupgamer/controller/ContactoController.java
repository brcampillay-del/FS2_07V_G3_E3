package cl.duoc.levelupgamer.controller;

import cl.duoc.levelupgamer.model.MensajeSoporte;
import cl.duoc.levelupgamer.repository.MensajeSoporteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/contacto")
@Tag(name = "Soporte", description = "Gestión de consultas y mensajes enviados a soporte")
public class ContactoController {

    private final MensajeSoporteRepository repository;

    public ContactoController(MensajeSoporteRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @Operation(summary = "Enviar consulta a soporte (Público)")
    public ResponseEntity<MensajeSoporte> enviarMensaje(@Valid @RequestBody MensajeSoporte mensaje) {
        mensaje.setFechaEnvio(LocalDateTime.now());
        MensajeSoporte guardado = repository.save(mensaje);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @GetMapping
    @Operation(summary = "Listar todos los mensajes de soporte (Solo ADMIN)", security = @SecurityRequirement(name = "bearerAuth"))
    public List<MensajeSoporte> listarMensajes() {
        return repository.findAllByOrderByFechaEnvioDesc();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar mensaje atendido (Solo ADMIN)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> eliminarMensaje(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}