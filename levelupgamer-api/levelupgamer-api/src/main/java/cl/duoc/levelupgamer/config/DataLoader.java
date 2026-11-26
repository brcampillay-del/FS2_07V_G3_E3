package cl.duoc.levelupgamer.config;

import cl.duoc.levelupgamer.model.Producto;
import cl.duoc.levelupgamer.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner cargarDatosIniciales(ProductoRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                List<Producto> catalogoInicial = List.of(
                    crearProducto("JM001", "Catan", 29990, "Juegos de Mesa",
                        "Un clásico juego de estrategia donde los jugadores compiten por colonizar y desarrollar la isla de Catan.",
                        "/img/catan.jpg"),

                    crearProducto("JM002", "Carcassonne", 24990, "Juegos de Mesa",
                        "Un juego de colocación de fichas donde los jugadores construyen el paisaje alrededor de la fortaleza medieval de Carcassonne.",
                        "/img/carcassonne.jpg"),

                    crearProducto("JM003", "Dixit", 27990, "Juegos de Mesa",
                        "Un juego de mesa creativo y narrativo con cartas ilustradas donde la imaginación es clave para adivinar la historia.",
                        "/img/dixit.jpg"),

                    crearProducto("CO001", "PlayStation 5", 549990, "Consolas",
                        "La consola de última generación de Sony, que ofrece gráficos impresionantes y tiempos de carga ultrarrápidos.",
                        "/img/play.jpg"),

                    crearProducto("CO002", "Xbox Series X", 529990, "Consolas",
                        "La consola más potente de Microsoft con gráficos 4K nativos, arquitectura Velocity y tiempos de carga mínimos.",
                        "/img/xbox.jpg"),

                    crearProducto("CG001", "PC Gamer ASUS ROG Strix", 1299990, "Consolas",
                        "Un potente equipo diseñado para los gamers más exigentes, equipado con los últimos componentes para un rendimiento excepcional.",
                        "/img/pc.jpg"),

                    crearProducto("AC001", "Controlador Inalámbrico Xbox Series X", 59990, "Periféricos",
                        "Ofrece una experiencia de juego cómoda con botones mapeables y una respuesta táctil mejorada.",
                        "/img/control.jpg"),

                    crearProducto("AC002", "Auriculares Gamer HyperX Cloud II", 79990, "Periféricos",
                        "Proporcionan un sonido envolvente de calidad con un micrófono desmontable y almohadillas de espuma viscoelástica.",
                        "/img/audifonos.jpg"),

                    crearProducto("AC003", "Teclado Mecánico Gamer RGB", 69990, "Periféricos",
                        "Teclado mecánico con switches táctiles de alta respuesta e iluminación RGB personalizable para sesiones competitivas.",
                        "/img/teclado.jpg"),

                    crearProducto("SG001", "Silla Gamer Secretlab Titan", 349990, "Periféricos",
                        "Diseñada para el máximo confort, esta silla ofrece un soporte ergonómico y personalización ajustable para sesiones prolongadas de juego.",
                        "/img/silla.jpg"),

                    crearProducto("MS001", "Mouse Gamer Logitech G502 HERO", 49990, "Periféricos",
                        "Con sensor de alta precisión y botones personalizables, este mouse es ideal para gamers que buscan un control preciso.",
                        "/img/mouse.jpg"),

                    crearProducto("MP001", "Mousepad Razer Goliathus Extended Chroma", 29990, "Periféricos",
                        "Ofrece un área de juego amplia con iluminación RGB personalizable, asegurando una superficie suave y uniforme.",
                        "/img/mousepad.jpg"),

                    crearProducto("PP001", "Polera Gamer Personalizada 'Level-Up'", 14990, "Poleras Personalizadas",
                        "Una camiseta cómoda y estilizada, con la posibilidad de personalizarla con tu gamer tag o diseño favorito.",
                        "/img/polera.jpg"),

                    crearProducto("PP002", "Polerón Gamer 'Level-Up' Edición Especial", 29990, "Poleras Personalizadas",
                        "Polerón con capucha de algodón premium estampado con el emblema neón de Level-Up Gamer.",
                        "/img/poleron.jpg"),

                    crearProducto("PP003", "Gorra Gamer 'Level-Up'", 12990, "Poleras Personalizadas",
                        "Gorra ajustable estilo snapback con bordado en alta definición de Level-Up Gamer.",
                        "/img/gorra.jpg")
                );

                repo.saveAll(catalogoInicial);
            }
        };
    }

    private Producto crearProducto(String codigo, String nombre, Integer precio, String categoria, String descripcion, String imagen) {
        Producto p = new Producto();
        p.setCodigo(codigo);
        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setCategoria(categoria);
        p.setDescripcion(descripcion);
        p.setImagen(imagen);
        return p;
    }
}