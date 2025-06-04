package com.cooperative.voting.config;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Requisição processada com sucesso"),
        @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Recurso não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
})
public @interface StandardApiResponses {
}
