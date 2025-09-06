package appconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("model.entity")
@EnableJpaRepositories("model.dao")
@ComponentScan(basePackages = {
        "web.controller",
        "service",
        "model.dao",
        "model.entity",
        "model.util"
})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
