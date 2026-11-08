package utp.pe.patitas;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import utp.pe.patitas.model.Usuario;
import utp.pe.patitas.repository.UsuarioRepository;

@SpringBootApplication
public class PatitasApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatitasApplication.class, args);
	}
	@Bean
	CommandLineRunner datos(UsuarioRepository repo) {
		return args -> repo.save(new Usuario("ana", "Patitas2026", "Ana"));
	}


}
