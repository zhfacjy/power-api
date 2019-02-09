package power.api.config;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static com.google.common.base.Predicates.or;
import static java.util.Collections.singletonList;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class Swagger2Configuration {
    @Bean
    public Docket buildDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(buildApiInf())
                .select()
                .apis(RequestHandlerSelectors.basePackage("power.api.controller"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(singletonList(apiKey()))
                .securityContexts(singletonList(securityContext()));
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(paths())
                .build();
    }

    private Predicate<String> paths() {
        return or(
                regex("/dict.*"),
                regex("/some.*"));
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return singletonList(
                new SecurityReference("x-access-token", authorizationScopes));
    }

    private ApiKey apiKey() {
        return new ApiKey("x-access-token", "x-access-token", "header");
    }

    private ApiInfo buildApiInf() {
        Contact contact = new Contact("Alex XU", "http://blog.csdn.net/xubaifu1997", "xubaifu97@gmail.com");
        return new ApiInfoBuilder()
                .title("电力运维云平台API文档")
                .contact(contact)
                .version("1.0")
                .build();
    }
}