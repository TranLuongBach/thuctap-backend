package com.example.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    public static final String SECURITY_SCHEME = "bearerAuth";
    private static final String API_TITLE = "Task Management API";
    private static final String API_VERSION = "1.0.0";
    private static final String API_DESCRIPTION =
            "API phục vụ quản lý người dùng, dự án và công việc trong hệ thống";

    private static final String CONTACT_NAME = "Backend Intern";
    private static final String CONTACT_EMAIL = "intern@example.com";

    private static final String LICENSE_NAME = "Apache 2.0";
    private static final String LICENSE_URL = "http://springdoc.org";

    private static final String LOCAL_SERVER_URL = "http://localhost:8080";
    private static final String LOCAL_SERVER_DESCRIPTION = "Local Development Server";

    @Bean
    public OpenAPI openApiDefinition() {
        return new OpenAPI()
                .info(buildApiInfo())
                .servers(buildServers())
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME))
                .components(
                        new Components().addSecuritySchemes(SECURITY_SCHEME, buildSecurityScheme())
                );
    }

    private Info buildApiInfo() {
        return new Info()
                .title(API_TITLE)
                .version(API_VERSION)
                .description(API_DESCRIPTION)
                .contact(
                        new Contact()
                                .name(CONTACT_NAME)
                                .email(CONTACT_EMAIL)
                )
                .license(
                        new License()
                                .name(LICENSE_NAME)
                                .url(LICENSE_URL)
                );
    }

    private List<Server> buildServers() {
        return List.of(
                new Server()
                        .url(LOCAL_SERVER_URL)
                        .description(LOCAL_SERVER_DESCRIPTION)
        );
    }

    private SecurityScheme buildSecurityScheme() {
        return new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Nhập JWT token vào ô bên dưới");
    }
}