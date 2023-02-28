package com.sep.coffeemanagement.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
  info = @Info(
    title = "CoffeeHouseSEP API",
    version = "${api.version}",
    contact = @Contact(
      name = "CoffeeHouseSEPBackend",
      email = "CoffeeHouseSEPBackend@gmail.com",
      url = "https://github.com/CoffeeHouseSEP"
    )
    // ,
    // license = @License(
    // name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0"
    // ),
    // termsOfService = "${tos.uri}", description = "${api.description}"), servers =
    // @Server(url = "${api.server.url}", description = "Production"
  )
)
@SecurityScheme(
  name = "Bearer Authentication",
  type = SecuritySchemeType.HTTP,
  bearerFormat = "JWT",
  scheme = "Bearer"
)
public class OpenApiDocConfig {}
