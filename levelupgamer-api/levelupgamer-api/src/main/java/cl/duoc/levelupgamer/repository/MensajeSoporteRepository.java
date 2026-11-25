package cl.duoc.levelupgamer.repository;

import cl.duoc.levelupgamer.model.MensajeSoporte;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MensajeSoporteRepository extends JpaRepository<MensajeSoporte, Long> {
    List<MensajeSoporte> findAllByOrderByFechaEnvioDesc();
}