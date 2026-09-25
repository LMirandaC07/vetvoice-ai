# 🐾 VetVoice AI

API backend para gestão de uma clínica veterinária, construída como projeto de portfólio para explorar **Java, Spring Boot, PostgreSQL, concorrência e IA generativa**.

O sistema gerencia clientes, pets, veterinários e agendamentos. Além do CRUD, protege horários contra reservas concorrentes e possui integração inicial com LLM por meio do Spring AI.

## ✨ Principais recursos

- Cadastro de clientes, pets e veterinários
- Criação, confirmação e cancelamento de consultas
- Validação de dados com Bean Validation
- Tratamento centralizado de erros
- Migrations versionadas com Flyway
- PostgreSQL executado com Docker Compose
- Proteção contra **double-booking** no serviço e no banco
- Teste de concorrência com 10 requisições simultâneas
- Endpoint experimental de assistente com Spring AI + OpenAI

## 🛠️ Tecnologias

`Java 21` · `Spring Boot 3` · `Spring Data JPA` · `PostgreSQL` · `Flyway` · `Docker` · `Spring AI` · `JUnit 5` · `Maven`

## 🏗️ Arquitetura

O projeto segue um **monólito modular organizado por domínio**, evitando separar toda a aplicação apenas por tipo técnico.

```text
HTTP → Controller → Service → Repository → PostgreSQL
```
```text
src/main/java/com/vetvoice
├── ai/              # integração com IA
├── appointment/     # regras de agendamento
├── client/          # clientes
├── common/          # componentes compartilhados e exceptions
├── pet/             # animais
└── veterinarian/    # veterinários
```

## 🔒 Destaque técnico: concorrência

Um problema importante em sistemas de agenda é impedir que duas pessoas reservem o mesmo profissional no mesmo horário.

O VetVoice resolve isso em duas camadas:

1. O `AppointmentService` verifica previamente se o horário está disponível.
2. O PostgreSQL possui o índice único parcial `uq_appointment_vet_slot`, que mantém a regra mesmo se duas requisições chegarem simultaneamente.

O `AppointmentConcurrencyTest` dispara **10 threads ao mesmo tempo** para o mesmo horário. O comportamento esperado é exatamente uma reserva bem-sucedida.

## 🚀 Executando localmente

### Pré-requisitos

- JDK 21+
- Docker Desktop
- Maven (ou Maven integrado ao IntelliJ IDEA)

### 1. Banco de dados
```bash
docker compose up -d
```

### 2. OpenAI (opcional)

A chave só é necessária para testar `/api/ai/ask`. Nunca coloque uma chave real no Git.

PowerShell:

```powershell
$env:OPENAI_API_KEY="sua-chave"
```

Linux/macOS:

```bash
export OPENAI_API_KEY="sua-chave"
```

### 3. Aplicação

No IntelliJ, execute `VetVoiceApplication`, ou pelo terminal:

```bash
mvn spring-boot:run
```

A API ficará disponível em `http://localhost:8080`.

## 📡 Endpoints principais

| Método | Endpoint | Função |
|---|---|---|
| `POST` | `/api/clients` | Cadastrar cliente |
| `POST` | `/api/pets` | Cadastrar pet |
| `POST` | `/api/veterinarians` | Cadastrar veterinário |
| `POST` | `/api/appointments` | Criar agendamento |
| `PATCH` | `/api/appointments/{id}/confirm` | Confirmar agendamento |
| `PATCH` | `/api/appointments/{id}/cancel` | Cancelar agendamento |
| `POST` | `/api/ai/ask` | Perguntar ao assistente da clínica |

## 🧪 Testes

```bash
mvn test
```

> O teste de concorrência utiliza PostgreSQL. Inicie o Docker Desktop e execute `docker compose up -d` antes dos testes de integração.

## 🗺️ Roadmap

- [x] API REST e persistência
- [x] Disponibilidade e proteção contra concorrência
- [x] Integração inicial com LLM
- [ ] Tool Calling para criar/cancelar agendamentos por IA
- [ ] RAG com informações da clínica
- [ ] Atendimento por voz
- [ ] Observabilidade, CI e deploy

## 💡 O que este projeto demonstra

Este projeto foi desenvolvido com foco em aprendizado e portfólio. Ele demonstra modelagem de domínio, criação de APIs REST, persistência relacional, migrations, tratamento de erros, concorrência, testes e integração de IA em uma aplicação Java.

## 👨‍💻 Autor

**Luis Miranda**  
Estudante de Análise e Desenvolvimento de Sistemas.

---

Se este projeto foi útil ou interessante, uma ⭐ no repositório é bem-vinda.