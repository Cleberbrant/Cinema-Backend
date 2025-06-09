# 🎬 Cinema Microservices - Trabalho TPPE

| Aluno                        | Matrícula   | Disciplina                                           |
|------------------------------|-------------|------------------------------------------------------|
| Cleber de Oliveira Brant     | 200061216   | Técnicas de Programação em Plataformas Emergentes     |

---

## Sobre o Projeto

Este projeto é um sistema completo para gerenciamento de um cinema, desenvolvido com **arquitetura de microserviços**. O backend, o serviço de autenticação e o frontend são desacoplados e se comunicam via REST, com autenticação baseada em JWT. O sistema foi evoluído a partir de um projeto de Orientação a Objetos (OO) e agora segue boas práticas de arquitetura, segurança, versionamento de banco e documentação.

O sistema permite o cadastro e gerenciamento de usuários, filmes, sessões, alimentos, pagamentos e toda a lógica de um cinema moderno, incluindo autenticação de administradores e clientes, operações CRUD completas e controle de acesso por roles.

---

## Arquitetura

### Nome

**Cinema Microservices Architecture**

### Microserviços

- **auth-cinema**: Responsável por autenticação, cadastro e gerenciamento de usuários.
- **cinema**: Backend principal, gerencia filmes, sessões, pagamentos, alimentos, etc.
- **frontend**: Interface web para interação do usuário.

Cada microserviço possui seu próprio banco de dados, e a comunicação entre eles é feita via HTTP/REST.

---

## Estrutura de Pastas

/cinema-spring
├── /auth-cinema # Microserviço de autenticação (Spring Boot)
│ ├── src/main/java/com/cleber/auth_cinema
│ ├── src/main/resources/application.properties
│ ├── src/main/resources/db/migration
│ └── Dockerfile
├── /cinema # Backend principal (Spring Boot)
│ ├── src/main/java/com/cleber/cinema
│ ├── src/main/resources/application.properties
│ ├── src/main/resources/db/migration
│ └── Dockerfile
├── /frontend # Frontend (React, Vue, Angular ou outro framework)
│ ├── src/
│ ├── package.json
│ └── Dockerfile
├── docker-compose.yml # Orquestração dos serviços
└── README.md # Este arquivo

---

## Principais Funcionalidades

- Cadastro e autenticação de usuários (admin e clientes) com JWT
- Cadastro e gerenciamento de filmes, sessões, salas, cinemas e localidades
- Gerenciamento de alimentos e vendas de alimentos
- Gerenciamento de pagamentos vinculados a usuários, filmes e alimentos
- Controle de acesso por roles (admin pode promover usuários, cadastrar filmes, etc.)
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
- **Swagger (SpringDoc OpenAPI)**
- **Docker**
- **Frontend:** React, Vue ou Angular (a definir)

---

## Ambientação do Sistema

### 1. Rodando com Docker

O projeto já inclui um `docker-compose.yml` que sobe todos os microserviços e bancos de dados necessários.

#### Etapas para subir com Docker

1. **Clone o repositório**
2. **Navegue até a raiz do projeto**
3. **Execute:**
    ```
    docker-compose up --build -d
    ```
    
Isso irá subir:
   - O banco de dados PostgreSQL de cada serviço
   - O backend (cinema) em `localhost:8080`
   - O serviço de autenticação em `localhost:8081`
   - O frontend em `localhost:3000` (ou porta definida no Dockerfile)

#### Parar os containers

docker-compose down

---

### 2. Rodando localmente (IDE)

#### Backend (cinema) e Auth-cinema

1. Instale o PostgreSQL 16 em sua máquina.
2. Crie os bancos de dados necessários (`cinema`, `auth_cinema`) e configure usuários e senhas conforme o `application.properties`.
3. Execute cada microserviço via IDE (classe principal).
4. O Flyway aplicará as migrations automaticamente ao iniciar cada serviço.

#### Frontend

1. Acesse a pasta do frontend:
    ```
    cd frontend
    ```
2. Instale as dependências:
    ```
    npm install
    ```
3. Suba o serviço:
    ```
    npm start
    ```
4. Acesse via navegador: [http://localhost:3000](http://localhost:3000)

---

## Migrations e Inicialização do Banco

- O projeto utiliza **Flyway** para versionamento do banco.
- Ao rodar a aplicação (via Docker ou IDE), as migrations são aplicadas automaticamente.
- Um usuário Admin padrão pode ser criado via migration:
    - **E-mail:** `admin@gmail.com`
    - **Senha:** `admin`
    - **Role:** `ROLE_ADMIN`

---

## Documentação da API

- **Backend (cinema):** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **Auth-cinema:** [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

---

## Fluxo de Autenticação

1. O usuário se cadastra ou faz login no serviço de autenticação.
2. O frontend recebe um token JWT.
3. O token é enviado nas requisições ao backend para autenticação/autorização.

---

## Considerações Finais

- O projeto é escalável e pode ser adaptado para novas funcionalidades.
- A comunicação entre microserviços é feita via REST e JWT.
- O frontend pode ser desenvolvido em qualquer tecnologia moderna (React, Vue, Angular).

---
