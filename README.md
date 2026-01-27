# 🎓 Start Students API

Uma API REST completa para gerenciamento de estudantes construída com **Spring Boot 3.5.9** e **Java 17**. Oferece autenticação segura com JWT, operações CRUD robustas, paginação inteligente e integração com PostgreSQL.

<p>
  <a href="https://spring.io/projects/spring-boot"><img alt="Spring Boot" src="https://img.shields.io/badge/Spring%20Boot-3.5.9-6DB33F?logo=springboot&logoColor=white" /></a>
  <a href="https://www.java.com/"><img alt="Java" src="https://img.shields.io/badge/Java-17-ED8B00?logo=java&logoColor=white" /></a>
  <a href="https://maven.apache.org/"><img alt="Maven" src="https://img.shields.io/badge/Maven-3.9+-C71A36?logo=apache-maven&logoColor=white" /></a>
  <a href="https://www.postgresql.org/"><img alt="PostgreSQL" src="https://img.shields.io/badge/PostgreSQL-15+-336791?logo=postgresql&logoColor=white" /></a>
  <a href="https://spring.io/projects/spring-security"><img alt="Spring Security" src="https://img.shields.io/badge/Spring%20Security-JWT-6DB33F?logo=springsecurity&logoColor=white" /></a>
  <a href="https://projectlombok.org/"><img alt="Lombok" src="https://img.shields.io/badge/Lombok-1.18+-FF6B6B" /></a>
  <a href="https://jwt.io/"><img alt="JWT" src="https://img.shields.io/badge/JWT-Auth-000000?logo=jsonwebtokens&logoColor=white" /></a>
  <a href="https://swagger.io/"><img alt="Swagger/OpenAPI" src="https://img.shields.io/badge/Swagger-OpenAPI%203.0-85EA2D?logo=swagger&logoColor=white" /></a>
</p>

---

## 📋 Índice

- [🎯 Visão Geral](#-visão-geral)
- [🏗️ Arquitetura](#-arquitetura)
- [🔧 Tecnologias](#-tecnologias)
- [🚀 Como Executar](#-como-executar)
- [🗄️ Banco de Dados](#-banco-de-dados)
- [🔌 Endpoints da API](#-endpoints-da-api)
- [📁 Estrutura do Projeto](#-estrutura-do-projeto)
- [🔐 Segurança](#-segurança)
- [🧪 Testes](#-testes)
- [📚 Documentação](#-documentação)
- [👨🏻‍💻 Autor](#-autor)

---

## 🎯 Visão Geral

**Start Students API** é uma solução backend para gerenciar registros de estudantes com funcionalidades completas:

✅ **Autenticação & Autorização** — JWT Bearer com refresh tokens  
✅ **CRUD Completo** — Create, Read, Update, Delete de estudantes  
✅ **Paginação Inteligente** — Suporte a `page`, `size`, `search`, `searchType`  
✅ **Busca Avançada** — Filtros por nome, CPF e email  
✅ **Validação Robusta** — Validação de entradas com Bean Validation  
✅ **Clean Architecture** — Separação clara de responsabilidades  
✅ **Documentação Automática** — Swagger/OpenAPI para exploração interativa  
✅ **Tratamento de Erros** — Respostas HTTP padronizadas  

---

## 🏗️ Arquitetura

A aplicação segue uma arquitetura em **camadas** com padrão de **Hexagonal Architecture**:

```
┌─────────────────────────────────────────────────────────────┐
│                    CLIENT (Angular)                          │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                    CONTROLLERS                               │
│  StudentController | AuthController | HealthController      │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                    USE CASES                                 │
│  ListStudentsUseCase | CreateStudentUseCase | DeleteUseCase │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                    SERVICES                                  │
│  StudentService | AuthService | JwtTokenProvider            │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│                    REPOSITORIES                              │
│  StudentRepository | UserRepository                          │
└────────────────────────┬────────────────────────────────────┘
                         │
┌────────────────────────▼────────────────────────────────────┐
│              DATABASE (PostgreSQL)                           │
│  students | users | audit_logs                              │
└─────────────────────────────────────────────────────────────┘
```

---

## 🔧 Tecnologias

| Categoria | Tecnologia | Versão |
|-----------|-----------|--------|
| **Framework** | Spring Boot | 3.5.9 |
| **Linguagem** | Java | 17 |
| **Build Tool** | Maven | 3.9+ |
| **Banco de Dados** | PostgreSQL | 15+ |
| **Autenticação** | JWT (JJWT) | 0.11.5 |
| **ORM** | Spring Data JPA | 6.2+ |
| **Utilidades** | Lombok | 1.18+ |
| **Documentação** | Springdoc OpenAPI | 2.0+ |
| **Validação** | Bean Validation | 8+ |
| **Testes** | JUnit 5 | 5.9+ |

---

## 🚀 Como Executar

### 📦 Pré-requisitos

- **Java 17+** — [Download](https://www.oracle.com/java/technologies/downloads/)
- **Maven 3.9+** — [Download](https://maven.apache.org/download.cgi)
- **PostgreSQL 15+** — [Download](https://www.postgresql.org/download/)
- **Git** — Para clonar o repositório

### 🐱‍🏍 Início Rápido

```bash
# 1. Clonar repositório
git clone https://github.com/ItaloRochaj/start-back-dev.git
cd start-back-dev

# 2. Criar banco de dados
# No PostgreSQL:
# CREATE DATABASE projects;

# 3. Instalar dependências
mvn clean install

# 4. Executar aplicação
mvn spring-boot:run

# ✅ API em http://localhost:8080
```

### 🔧 Configuração de Banco de Dados

Edite `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/projects
spring.datasource.username=postgres
spring.datasource.password=1234
```

---

## 🗄️ Banco de Dados

### Tabelas Principais

```sql
-- Usuários
CREATE TABLE users (
  id UUID PRIMARY KEY,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  first_name VARCHAR(100),
  last_name VARCHAR(100),
  phone VARCHAR(20),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Estudantes
CREATE TABLE students (
  id UUID PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  cpf VARCHAR(11) UNIQUE NOT NULL,
  email VARCHAR(255) UNIQUE,
  phone VARCHAR(20),
  status VARCHAR(50) DEFAULT 'ATIVO',
  user_id UUID REFERENCES users(id),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## 🔌 Endpoints da API

### Base URL
```
http://localhost:8080/api
```

### 👤 Autenticação

**POST /auth/register** — Registrar novo usuário
```json
{
  "email": "aluno@example.com",
  "password": "Senha123!",
  "firstName": "João",
  "lastName": "Silva"
}
```

**POST /auth/login** — Fazer login
```json
{
  "email": "aluno@example.com",
  "password": "Senha123!"
}
```

Resposta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 86400000
}
```

### 🎓 Estudantes

**GET /students** — Listar com paginação
```
?page=0&size=4&search=&searchType=name
```

Resposta:
```json
{
  "success": true,
  "message": "Alunos listados",
  "data": {
    "content": [ { ... } ],
    "page": 0,
    "size": 4,
    "totalElements": 7,
    "totalPages": 2,
    "first": true,
    "last": false
  }
}
```

**GET /students/{id}** — Obter detalhes

**POST /students** — Criar estudante (requer autenticação)

**PUT /students/{id}** — Atualizar estudante (requer autenticação)

**DELETE /students/{id}** — Deletar estudante (requer autenticação)

---

## 📁 Estrutura do Projeto

```
start-back-dev/
├── src/main/java/start/students/
│   ├── StudentsApplication.java
│   ├── adapters/
│   │   ├── inbound/
│   │   │   ├── controllers/
│   │   │   │   ├── StudentController.java
│   │   │   │   └── AuthController.java
│   │   │   ├── http/
│   │   │   │   └── ApiResponse.java
│   │   │   └── security/
│   │   │       ├── JwtAuthenticationFilter.java
│   │   │       └── SecurityConfig.java
│   │   └── outbound/
│   │       └── persistence/
│   │           ├── repositories/
│   │           │   ├── StudentRepository.java
│   │           │   └── UserRepository.java
│   │           └── entities/
│   │               ├── Student.java
│   │               └── User.java
│   ├── core/
│   │   ├── application/
│   │   │   ├── dtos/
│   │   │   ├── mappers/
│   │   │   └── usecases/
│   │   ├── domain/
│   │   │   ├── entities/
│   │   │   ├── exceptions/
│   │   │   └── ports/
│   │   └── ports/
│   └── config/
├── src/main/resources/
│   ├── application.properties
│   ├── application-dev.properties
│   └── application-prod.properties
├── src/test/java/
│   └── start/students/
├── pom.xml
└── README.md
```

---

## 🔐 Segurança

### Autenticação JWT

Adicione o token no header das requisições:
```http
Authorization: Bearer {seu_token_jwt}
```

### Endpoints Protegidos
- `POST /api/students` — Requer autenticação
- `PUT /api/students/{id}` — Requer autenticação
- `DELETE /api/students/{id}` — Requer autenticação

### Endpoints Públicos
- `GET /api/students` — Público (com paginação)
- `GET /api/students/{id}` — Público
- `POST /api/auth/register` — Público
- `POST /api/auth/login` — Público

---

## 🧪 Testes

```bash
# Executar todos os testes
mvn test

# Executar teste específico
mvn test -Dtest=StudentControllerTest

# Com cobertura de código
mvn clean test jacoco:report
```

---

## 📚 Documentação

### Swagger/OpenAPI

Quando a API está rodando, acesse:
```
http://localhost:8080/swagger-ui.html
```

---

## 🚨 Troubleshooting

### Erro: "Connection refused"
```bash
# Verificar PostgreSQL
pg_isready -h localhost -p 5432

# Iniciar PostgreSQL (Windows)
pg_ctl -D "C:\Program Files\PostgreSQL\15\data" start
```

### Erro: "401 Unauthorized"
Certifique-se de enviar o token JWT no header `Authorization`.

---

## 👨🏻‍💻 Autor

**Italo Rocha** — [@ItaloRochaj](https://github.com/ItaloRochaj)

---

**Versão:** 1.0.0 | **Última atualização:** January 2026