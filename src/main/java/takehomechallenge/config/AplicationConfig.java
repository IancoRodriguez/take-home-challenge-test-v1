package takehomechallenge.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class AplicationConfig {



    // Open api confi

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("REST API")
                        .version("1.0")
                        .description("REST API Documentation")
                        .contact(new Contact()
                                .name("Ianco Rodriguez")
                                .email("ianco@gmail.com")));
    }
}
