## Voto Cooperativo API

Este projeto implementa uma API REST para gerenciamento de pautas, sessões de votação e votos em um sistema cooperativo.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.3.2
- Gradle
- Docker e Docker Compose
- PostgreSQL
- Springdoc OpenAPI (Swagger)
- JUnit 5 + Mockito para testes


## Como Executar

Use o Docker Compose, ele irá subir a aplicação junto com o banco de dados PostgreSQL configurado:

```bash
docker-compose up --build
````

Isso iniciará a aplicação principal `voto-cooperativo-api` na porta `8080`, a aplicação `user-service-api` na porta `8081` e o PostgreSQL na porta padrão `5432`.


## API Documentation (Swagger)

A aplicação principal, **voto-cooperativo-api**, possui documentação Swagger automática, acessível em:

* [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
* [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

A API externa de usuários, **user-service-api**, estará disponível na porta 8081 e também possui documentação Swagger, acessível em:

* [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
* [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

Use essas interfaces para explorar os endpoints, visualizar modelos e testar as APIs diretamente pelo navegador.

---

## Collection Postman

Foi disponibilizada uma collection completa para o Postman com todos os endpoints e exemplos configurados.

Você pode baixá-la aqui:  
[📥 Download Collection Postman (voto-cooperativo.postman_collection.json)](docs/voto-cooperativo.postman_collection.json)


Para facilitar os testes de votos, uma lista com CPFs válidos e inválidos para votação.

### CPFs válidos para votação (último dígito PAR):

- 12345678902
- 98765432124
- 11111111116

### CPFs não aptos a votar (último dígito ÍMPAR):

- 12345678901
- 98765432123
- 11111111115

Você pode copiar esses CPFs diretamente dos comentários para realizar novos testes.


## API Externa de Usuário (user-service-api)

A aplicação também consome uma API externa simulada, chamada `user-service-api`, para validação do status dos usuários (habilitado ou não para votar). Essa API simulada também possui Swagger disponível para consulta.

---

## Testes Unitários

O projeto inclui testes unitários para os principais serviços da aplicação, utilizando JUnit 5 e Mockito.

Esses testes cobrem casos de sucesso e falha para criação de pautas, abertura e fechamento de sessões de votação, além do fluxo de votação com validação de usuários.

Para executar os testes localmente, utilize:

```bash
./gradlew test
```

---

Obrigado por avaliar o projeto!

---
