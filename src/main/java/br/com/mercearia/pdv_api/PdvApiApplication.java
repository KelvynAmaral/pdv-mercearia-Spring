package br.com.mercearia.pdv_api;

import br.com.mercearia.pdv_api.model.Role;
import br.com.mercearia.pdv_api.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PdvApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PdvApiApplication.class, args);
    }

    @Bean
    CommandLineRunner run(RoleRepository roleRepository) {
        return args -> {
            // Insere as roles no banco de dados se elas não existirem
            if (roleRepository.findByNome("ROLE_ADMIN").isEmpty()) {
                roleRepository.save(new Role("ROLE_ADMIN"));
            }
            if (roleRepository.findByNome("ROLE_VENDEDOR").isEmpty()) {
                roleRepository.save(new Role("ROLE_VENDEDOR"));
            }
        };
    }
}
