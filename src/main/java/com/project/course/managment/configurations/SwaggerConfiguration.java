package com.project.course.managment.configurations;

import com.project.course.managment.constants.documentation.GeneralApiConstants;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title(GeneralApiConstants.API_NAME_SWAGGER)
                        .description(GeneralApiConstants.API_DESCRIPTION_SWAGGER)
                        .version("v0.0.1"));
    }

}

