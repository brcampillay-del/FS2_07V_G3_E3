package cl.duoc.levelupgamer.controller;

import cl.duoc.levelupgamer.model.Producto;
import cl.duoc.levelupgamer.repository.ProductoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Gestión del catálogo y administración de inventario")
public class ProductoController {

    private final ProductoRepository repository;

    public ProductoController(ProductoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @Operation(summary = "Listar todos los productos (Público)")
    public List<Producto> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un producto por ID (Público)")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear producto (Solo ADMIN)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Producto> crear(@Valid @RequestBody Producto producto) {
        if (producto.getCodigo() == null || producto.getCodigo().isBlank()) {
            producto.setCodigo("NEW-" + (int)(Math.random() * 1000));
        }
        Producto guardado = repository.save(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto (Solo ADMIN)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @Valid @RequestBody Producto datos) {
        return repository.findById(id)
                .map(existente -> {
                    existente.setCodigo(datos.getCodigo());
                    existente.setNombre(datos.getNombre());
                    existente.setPrecio(datos.getPrecio());
                    existente.setCategoria(datos.getCategoria());
                    existente.setDescripcion(datos.getDescripcion());
                    existente.setImagen(datos.getImagen());
                    return ResponseEntity.ok(repository.save(existente));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto (Solo ADMIN)", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}