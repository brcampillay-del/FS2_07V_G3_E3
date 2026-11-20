package cl.duoc.levelupgamer.repository;

import cl.duoc.levelupgamer.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}