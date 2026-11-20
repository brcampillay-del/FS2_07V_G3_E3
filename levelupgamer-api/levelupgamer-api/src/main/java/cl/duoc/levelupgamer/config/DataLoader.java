package cl.duoc.levelupgamer.config;

import cl.duoc.levelupgamer.model.Producto;
import cl.duoc.levelupgamer.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner cargarDatosIniciales(ProductoRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                Producto p1 = new Producto();
                p1.setCodigo("JM001");
                p1.setNombre("Catan");
                p1.setPrecio(29990);
                p1.setCategoria("Juegos de Mesa");
                p1.setDescripcion("Un clásico juego de estrategia donde los jugadores compiten por colonizar la isla de Catan.");
                p1.setImagen("/img/catan.jpg");

                Producto p2 = new Producto();
                p2.setCodigo("CO001");
                p2.setNombre("PlayStation 5");
                p2.setPrecio(549990);
                p2.setCategoria("Consolas");
                p2.setDescripcion("La consola de última generación de Sony con tiempos de carga ultrarrápidos.");
                p2.setImagen("/img/play.jpg");

                repo.save(p1);
                repo.save(p2);
            }
        };
    }
}