# 🎬 Cinema Backend - Trabalho TPPE

| Aluno                        | Matrícula   | Disciplina                                           |
|------------------------------|-------------|------------------------------------------------------|
| Cleber de Oliveira Brant     | 200061216   | Técnicas de Programação em Plataformas Emergentes     |


## Sobre o Projeto

Este projeto é uma **API RESTful para gerenciamento de um cinema**, desenvolvida em **Spring Boot** com autenticação JWT, integração com banco de dados PostgreSQL e versionamento de schema via Flyway.

O trabalho consiste na **evolução do projeto de Orientação a Objetos (OO)**, feito pelos alunos Cleber Brant e Wesley Lira, onde  o seguiriamos com tema e modelagem para um gerenciamento de um cinema, originalmente criado para fins acadêmicos e agora expandido para um contexto real de API Restfull, com boas práticas de arquitetura, segurança e documentação.

Link do repositório orginal: https://github.com/Weslin-0101/TP2


O sistema permite o cadastro e gerenciamento de usuários, filmes, sessões, alimentos, pagamentos e toda a lógica de um cinema moderno, incluindo relacionamento entre entidades, autenticação de administradores e clientes, e operações CRUD completas.<br/>


A modelagem para o gerenciamento de Usuarios, tanto Admin quanto Clientes, foi refeita para melhores praticas de gerenciamentos de contas, onde a especialização(Pessoa, Cliente e Administração) foi substituida pela classe Usuario, atribuindo o conceito de Roles. O restante da modelagem, permaneceu igual, porém, com alterações em seus atributos para que fossem otimizados.

---

## Principais Funcionalidades

- Cadastro e autenticação de usuários (admin e clientes) com JWT
- Cadastro e gerenciamento de filmes, sessões, salas, cinemas e localidades
- Gerenciamento de alimentos e vendas de alimentos
- Gerenciamento de pagamentos vinculados a usuários, filmes e alimentos(Simulação no sistema)
- Controle de acesso (admin pode promover usuários, cadastrar filmes, etc.)
- Documentação de API via Swagger
- Versionamento de banco de dados com Flyway

---

## Tecnologias Utilizadas

- **Java 21**
- **Maven**
- **Spring Boot 3**
- **Spring Data JPA**
- **Spring Security + JWT**
- **PostgreSQL 16**
- **Flyway**
- **Lombok**
- **Swagger**
- **Docker**

---

## Ambiente de Desenvolvimento

Existem duas formas de subir o sistema para desenvolvimento, sendo elas, Docker(Sem necessidade de muitas configurações) e pela própria IDE(Mais configurações de ambiente a serem feitas).

### 1. Rodando com Docker

O projeto já inclui um `docker-compose.yml` que sobe **tanto o banco de dados PostgreSQL quanto a aplicação Spring Boot**.

#### Etapas para subir com Docker

1. **Clone o repositório**
2. **Navegue até a raiz do projeto**
3. **Execute:**
    ```
    docker-compose up --build -d
    ```
    
Isso irá subir:
   - O banco de dados PostgreSQL em `localhost:5432`
   - A aplicação em `localhost:8080`

**Credenciais padrão do banco:**

- Usuário: `postgres`
- Senha: `root`
- Banco: `cinema`

#### Parar os containers

docker-compose down

---

### 2. Rodando a aplicação diretamente pela IDE (Com banco local)

Se preferir rodar a aplicação pela sua IDE (IntelliJ, Eclipse, VSCode, etc):

1. **Instale o PostgreSQL 16** em sua máquina.

2. **Crie o banco de dados:**
    ```
    CREATE DATABASE cinema;
    ```

3. **Crie o usuário e senha (se desejar, use os padrões do projeto):**
    ```
    CREATE USER postgres WITH PASSWORD 'root';
    GRANT ALL PRIVILEGES ON DATABASE cinema TO postgres;
    ```

4. **Configure o arquivo `application.properties`:**
    ```
    spring.datasource.url=jdbc:postgresql://localhost:5432/cinema
    spring.datasource.username=postgres
    spring.datasource.password=root
    spring.jpa.hibernate.ddl-auto=none
    spring.flyway.locations=classpath:db/migration
    ```

5. **Execute a aplicação pela IDE** (classe `CinemaApplication`).

---

## Migrations e Inicialização do Banco

- O projeto utiliza **Flyway** para versionamento do banco.
- Ao rodar a aplicação (via Docker ou IDE), as migrations serão aplicadas automaticamente.
- Um usuário Admin padrão do sistema é criado via migration:
    - **E-mail:** `admin@gmail.com`
    - **Senha:** `admin`
    - **Role:** `ROLE_ADMIN`

---

## Documentação da API

- Acesse o Swagger UI em:  
  [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
  para explorar e testar todos os endpoints da API.

---

