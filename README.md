<div align="center">

# 🐾 VetVoice AI

### API REST para gestão de clínicas veterinárias com Java, Spring Boot e PostgreSQL

[![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)](https://www.docker.com/)
[![Build](https://github.com/LMirandaC07/vetvoice-ai/actions/workflows/ci.yml/badge.svg)](https://github.com/LMirandaC07/vetvoice-ai/actions)

</div>

## 📌 Sobre o projeto

O **VetVoice AI** é um projeto backend que desenvolvi para aplicar conceitos de desenvolvimento de APIs em um cenário de clínica veterinária.

A aplicação gerencia **clientes, pets, veterinários e consultas**, com persistência em PostgreSQL, validações, migrations e tratamento de conflitos de agendamento. Também inclui um experimento inicial com **Spring AI** para um futuro assistente da clínica.

> O foco do projeto é estudar uma API além do CRUD básico, incluindo regras de negócio, concorrência, testes e integração com IA.

## ✨ Funcionalidades

- 👤 Cadastro e gerenciamento de clientes
- 🐶 Cadastro e gerenciamento de pets
- 🩺 Cadastro e gerenciamento de veterinários
- 📅 Criação, confirmação e cancelamento de consultas
- ✅ Validação dos dados recebidos pela API
- 🗃️ Versionamento do banco com Flyway
- 🔒 Proteção contra agendamentos duplicados no mesmo horário
- 🧪 Teste de integração para requisições concorrentes
- 🤖 Endpoint experimental utilizando Spring AI

## 🛠️ Tecnologias

| Tecnologia | Uso no projeto |
|---|---|
| **Java 21** | Linguagem principal |
| **Spring Boot 3** | Estrutura da aplicação e API REST |
| **Spring Data JPA** | Persistência e acesso aos dados |
| **PostgreSQL** | Banco de dados relacional |
| **Flyway** | Migrations e versionamento do schema |
| **Docker Compose** | Ambiente local do PostgreSQL |
| **JUnit 5** | Testes automatizados |
| **Spring AI** | Integração experimental com IA |
| **Maven** | Build e gerenciamento de dependências |

## 🏗️ Estrutura

Os pacotes são organizados por domínio. Cada área mantém próximos os seus controllers, services, repositories, entidades e DTOs.

```text
src/main/java/com/vetvoice
├── ai/              # integração experimental com IA
├── appointment/     # consultas e regras de agendamento
├── client/          # clientes
├── common/          # exceptions e componentes compartilhados
├── pet/             # pets
└── veterinarian/    # veterinários
```

Fluxo principal de uma requisição:

```text
HTTP Request
     ↓
Controller → Service → Repository → PostgreSQL
```

## 🔒 Concorrência no agendamento

Um dos pontos que quis explorar foi o **double-booking**: duas requisições tentando reservar o mesmo veterinário para o mesmo horário.

O `AppointmentService` verifica previamente a disponibilidade para retornar uma resposta clara no fluxo normal. Porém, essa verificação sozinha não elimina uma condição de corrida.
Por isso, o PostgreSQL também possui uma restrição para impedir dois agendamentos ativos com a mesma combinação de veterinário e horário. Assim, o banco fornece a garantia final de consistência.

O `AppointmentConcurrencyTest` simula **10 tentativas simultâneas** para o mesmo horário e verifica que apenas uma reserva é persistida.

## 📡 Endpoints principais

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/clients` | Cadastra um cliente |
| `POST` | `/api/pets` | Cadastra um pet |
| `POST` | `/api/veterinarians` | Cadastra um veterinário |
| `POST` | `/api/appointments` | Cria uma consulta |
| `PATCH` | `/api/appointments/{id}/confirm` | Confirma uma consulta |
| `PATCH` | `/api/appointments/{id}/cancel` | Cancela uma consulta |
| `POST` | `/api/ai/ask` | Envia uma mensagem ao assistente experimental |

## 🚀 Executando o projeto

### Pré-requisitos

- JDK 21 ou superior
- Docker Desktop
- Maven (ou Maven integrado ao IntelliJ IDEA)

### 1. Clone o repositório

```bash
git clone https://github.com/LMirandaC07/vetvoice-ai.git
cd vetvoice-ai
```

### 2. Inicie o PostgreSQL

```bash
docker compose up -d
```

### 3. Inicie a aplicação

Pelo IntelliJ IDEA, execute `VetVoiceApplication` ou utilize:

```bash
mvn spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8080
```

### 4. Execute os testes

```bash
mvn test
```

## 🤖 Integração com IA

O endpoint `/api/ai/ask` é uma integração inicial com **Spring AI**. Atualmente, o assistente responde utilizando um prompt de sistema, mas ainda não executa operações como criar ou cancelar consultas.

Para utilizar esse endpoint, defina a variável de ambiente `OPENAI_API_KEY`. **Não adicione sua chave diretamente ao código ou ao Git.**

## 🗺️ Próximos passos

- [ ] Aumentar a cobertura de testes das regras de negócio
- [ ] Documentar a API com OpenAPI / Swagger
- [ ] Adicionar autenticação e autorização
- [ ] Permitir que o assistente consulte horários de forma controlada
- [ ] Realizar deploy da aplicação

## 🎯 Aprendizados

Durante o desenvolvimento, trabalhei principalmente com:

- organização de uma aplicação Spring Boot por domínio;
- modelagem e persistência de relacionamentos com JPA;
- migrations de banco de dados;
- separação de responsabilidades entre controller, service e repository;
- tratamento de erros e validações de entrada;
- consistência de dados diante de requisições concorrentes;
- testes de integração com PostgreSQL;
- primeiros passos com IA generativa em uma aplicação Java.

## 👨‍💻 Autor

**Luis Miranda**

Estudante de **Análise e Desenvolvimento de Sistemas** e desenvolvedor em formação com foco em backend.

[GitHub](https://github.com/LMirandaC07)

---

<div align="center">
Desenvolvido para estudo, prática e evolução do meu portfólio em desenvolvimento backend.
</div>
