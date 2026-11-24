package cl.duoc.levelupgamer.controller;

import cl.duoc.levelupgamer.model.Compra;
import cl.duoc.levelupgamer.repository.CompraRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/compras")
@Tag(name = "Compras", description = "Registro e historial de compras realizadas")
@SecurityRequirement(name = "bearerAuth")
public class CompraController {

    private final CompraRepository repository;

    public CompraController(CompraRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @Operation(summary = "Registrar una nueva compra (Usuario autenticado)")
    public ResponseEntity<?> registrarCompra(@RequestBody Compra compra, Authentication auth) {
        if (compra.getItems() == null || compra.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "El carrito no puede estar vacío."));
        }

        compra.setUsuarioEmail(auth.getName());
        compra.setFecha(LocalDateTime.now());

        Compra guardada = repository.save(compra);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
    }

    @GetMapping("/mis-compras")
    @Operation(summary = "Listar el resumen de compras del usuario autenticado")
    public List<Compra> misCompras(Authentication auth) {
        return repository.findByUsuarioEmailOrderByFechaDesc(auth.getName());
    }

    @GetMapping
    @Operation(summary = "Listar todas las compras de la tienda (Solo ADMIN)")
    public List<Compra> listarTodas() {
        return repository.findAllByOrderByFechaDesc();
    }
}