package egovframework.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                    .title("도서 대출 관리 시스템 API")
                    .description("eGovFrame 기반 도서 대출 관리 시스템 REST API 문서")
                    .version("1.0.0")
                );
    }
}
