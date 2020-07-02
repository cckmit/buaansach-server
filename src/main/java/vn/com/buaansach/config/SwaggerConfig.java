package vn.com.buaansach.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import vn.com.buaansach.security.jwt.TokenProvider;

import static java.util.Collections.singletonList;

@Configuration
@EnableSwagger2
@Profile("dev")
public class SwaggerConfig {
    TokenProvider tokenProvider;

    public SwaggerConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public Docket api() {
        final String swaggerToken = tokenProvider.generateTokenFromUsername("adminbas");
        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(singletonList(
                        new ParameterBuilder()
                                .name("Authorization")
                                .modelRef(new ModelRef("string"))
                                .parameterType("header")
                                .required(false)
                                .hidden(true)
                                .allowEmptyValue(true)
                                .defaultValue("Bearer " + swaggerToken)
                                .build()
                        )
                )
                .select()
                .apis(RequestHandlerSelectors.basePackage("vn.com.buaansach"))
                .paths(PathSelectors.any())
                .build();
    }
}
