package com.realthon.on.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local"),
                        new Server().url("https://api.realthon.com").description("Production")
                ))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .info(apiInfo())
                .tags(List.of(
                        new Tag().name("Auth API").description("로그인/회원가입/토큰 관련 API"),
                        new Tag().name("User API").description("유저 정보 및 프로필 관련 API"),
                        new Tag().name("AI (Groq) API").description("일기 감정 분석 및 유해성 평가 API")
                ));
    }

    private Info apiInfo() {
        return new Info()
                .title("ON Platform API 문서")
                .description("ON Platform의 인증, 유저, AI 분석 API 문서입니다.")
                .version("v1.0.0")
                .contact(new Contact().name("ON Team").email("support@realthon.com"))
                .license(new License().name("Proprietary").url("https://realthon.com"));
    }
}
