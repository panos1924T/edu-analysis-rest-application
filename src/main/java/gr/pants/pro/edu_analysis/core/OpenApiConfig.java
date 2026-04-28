package gr.pants.pro.edu_analysis.core;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
/*
  The practical effect is that Swagger UI shows an "Authorize" button where you paste your JWT token.
 */
@SecurityScheme(
        name = "Bearer Authentication",     // Must match @SecurityRequirement's name
        type = SecuritySchemeType.HTTP,     // Authentication is done via HTTP header
        bearerFormat = "JWT",               // Informational, tells Swagger UI the token format is JWT
        scheme = "bearer"                   // The HTTP scheme is Bearer, meaning Authorization: Bearer <token>
)
public class OpenApiConfig {

    /*
        Provides the metadata that appears in Swagger UI's header section —
        purely informational, no functional impact on the API itself
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("EduAnalysis API")
                        .version("1.0.0")
                        .description("""
                                REST API for managing the International, educational analysis registry.
                                Provides endpoints for managing analysts, users, and organizational data.
                                
                                Authentication is done via JWT Bearer tokens.
                                Obtain a token from /api/auth/authenticate before using secured endpoints.
                        """)
                        .contact(new Contact()
                                .name("PanTs @ CF")
                                .email("pants.ath@gmail.com")
                                .url("https://panos1924t.github.io/personal-cv/"))
                        .license(new License()
                                .name("CC0 1.0 Universal")
                                .url("https://creativecommons.org/publicdomain/zero/1.0")));
    }
}