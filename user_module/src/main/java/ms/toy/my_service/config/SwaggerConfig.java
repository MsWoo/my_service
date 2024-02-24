package ms.toy.my_service.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

//    @Bean
//    public GroupedOpenApi memberAPI() {
//        return GroupedOpenApi.builder()
//                .group("Member API")
//                .pathsToMatch("/api/v1/member/**")
//                .build();
//    }
//
//    @Bean
//    public GroupedOpenApi loginAPI() {
//        return GroupedOpenApi.builder()
//                .group("Login API")
//                .pathsToMatch("/api/v1/login/**")
//                .build();
//    }

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info().title("User Module").description("My Service User Module Swagger API 문서입니다.").version("v0.0.1"))
                .components(
                        new Components().addSecuritySchemes("accessToken", new SecurityScheme()
                                .type(Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(In.HEADER).name("Authorization")))
                .security(Arrays.asList(new SecurityRequirement().addList("accessToken")));
    }

}
