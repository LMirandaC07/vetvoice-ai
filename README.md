# 🐾 VetVoice AI

O VetVoice AI é uma API que estou desenvolvendo para praticar backend com Java e Spring Boot usando o cenário de uma clínica veterinária.

A ideia começou com um problema simples: organizar clientes, pets, veterinários e consultas. A partir disso, usei o projeto para estudar persistência com PostgreSQL, migrations e um problema que achei interessante em sistemas de agenda: duas requisições tentando reservar o mesmo horário.

## O que funciona hoje

- CRUD de clientes, pets e veterinários
- criação, confirmação e cancelamento de consultas
- validação dos dados recebidos pela API
- PostgreSQL com migrations do Flyway
- prevenção de dois agendamentos ativos para o mesmo veterinário e horário
- teste de integração que simula requisições concorrentes
- primeiro experimento com Spring AI para um assistente da clínica

## Tecnologias que estou praticando

- Java 21
- Spring Boot 3 e Spring Data JPA
- PostgreSQL e Flyway
- Docker Compose
- Maven e JUnit 5
- Spring AI

## Estrutura do projeto

Organizei os pacotes por área do sistema (`client`, `pet`, `veterinarian` e `appointment`). Cada área concentra controller, service, repository, entidade e DTOs relacionados.

O fluxo principal da API é:

```text
requisição HTTP -> controller -> service -> repository -> PostgreSQL
```

## O problema de concorrência que estudei

Só consultar o banco antes de salvar não garante que um horário esteja livre. Duas requisições podem fazer essa consulta quase ao mesmo tempo e ambas encontrarem o horário disponível.

Por isso mantive uma verificação no `AppointmentService`, para retornar um erro mais claro no caso comum, e também criei no PostgreSQL um índice único para `(veterinarian_id, scheduled_at)` enquanto a consulta não estiver cancelada. O banco fica responsável pela garantia final.

O `AppointmentConcurrencyTest` tenta criar 10 consultas simultaneamente no mesmo horário e verifica que apenas uma consegue ser salva.

## Como executar

Pré-requisitos: JDK 21+, Docker Desktop e Maven.

1. Inicie o PostgreSQL:

```bash
docker compose up -d
```

2. Execute `VetVoiceApplication` pelo IntelliJ ou use:

```bash
mvn spring-boot:run
```

3. Para executar os testes:

```bash
mvn test
```

A API roda em `http://localhost:8080`.

## IA (em desenvolvimento)

O endpoint `/api/ai/ask` é meu primeiro experimento com Spring AI. Neste momento ele apenas conversa usando um prompt de sistema; ele ainda **não cria nem cancela consultas**.

Para testar essa parte, configure `OPENAI_API_KEY` no ambiente antes de iniciar a aplicação. O restante da API não depende desse endpoint para representar as regras de agendamento.

## Endpoints principais

| Método | Endpoint | Função |
|---|---|---|
| `POST` | `/api/clients` | cadastrar cliente |
| `POST` | `/api/pets` | cadastrar pet |
| `POST` | `/api/veterinarians` | cadastrar veterinário |
| `POST` | `/api/appointments` | criar consulta |
| `PATCH` | `/api/appointments/{id}/confirm` | confirmar consulta |
| `PATCH` | `/api/appointments/{id}/cancel` | cancelar consulta |
| `POST` | `/api/ai/ask` | conversar com o assistente experimental |

## Próximos passos

- aumentar a cobertura de testes das regras de negócio
- documentar a API com OpenAPI/Swagger
- permitir que o assistente consulte horários de forma controlada
- adicionar autenticação
- fazer deploy da aplicação

## Autor

**Luis Miranda** — estudante de Análise e Desenvolvimento de Sistemas.

Projeto criado para estudo e evolução do meu portfólio de backend.
