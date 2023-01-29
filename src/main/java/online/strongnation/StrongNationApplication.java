package online.strongnation;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@AllArgsConstructor
public class StrongNationApplication {

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.version"));
        SpringApplication.run(StrongNationApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase() {

        return args -> {
           };
    }
}
