package com.shiavnskipayroll.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info=@Info(title="Shiavnski-Payroll",
                         description = "This Api is for Management Info"

))
@Configuration
public class SwaggerConfig {
}
