# Relationship

## Descrição
Este é um projeto de demonstração utilizando Spring Boot para gerenciar informações de clientes.

## Tecnologias Utilizadas
- **Java 17**: Linguagem de programação utilizada.
- **Spring Boot Starter Data JPA**: Para persistência de dados.
- **Spring Boot Starter Data REST**: Para criação de APIs RESTful.
- **Spring Boot Starter Web**: Para desenvolvimento de aplicações web.
- **Spring Boot DevTools**: Para facilitar o desenvolvimento com recarregamento automático.
- **PostgreSQL**: Banco de dados relacional utilizado.
- **Lombok**: Biblioteca para reduzir o código boilerplate.
- **Springdoc OpenAPI**: Para documentação de APIs.
- **AWS SDK for Java**: Para integração com serviços da AWS, especificamente S3.

## Configuração
### Banco de Dados
Configure as seguintes propriedades no arquivo `application.properties`:
```ini
spring.datasource.url={DB_URL}
spring.datasource.username={DB_USER}
spring.datasource.password={DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

## AWS

Configurações no arquivo `application.properties`:
```ini
    aws.region={AWS_REGION}
    aws.bucket={AWS_BUCKET_NAME}
```

## Licença
Este projeto está licenciado sob os termos da licença MIT.