package appconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {
        "model.entity",
        "security.entity",
        "security.entity"
})
@EnableJpaRepositories(basePackages = {
        "model.dao",
        "security.dao"
})
@ComponentScan(basePackages = {
        "web",
        "service",
        "model",
        "security",
        "swagger"
})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
