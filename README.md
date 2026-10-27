# 🗂️ Apollo Access Log

<p align="center">
  <strong>Registro e consulta de acessos à plataforma Apollo</strong>
</p>

<p align="center">
  <a href="https://github.com/ApolloOficial/Access-Log-Api/actions/workflows/ci.yml"><img alt="CI" src="https://github.com/ApolloOficial/Access-Log-Api/actions/workflows/ci.yml/badge.svg"></a>
  <img alt="Java 21" src="https://img.shields.io/badge/Java%2021-ED8B00">
  <img alt="Spring Boot" src="https://img.shields.io/badge/Spring%20Boot-6DB33F">
  <img alt="MongoDB" src="https://img.shields.io/badge/MongoDB-47A248">
</p>

A Access Log API fornece operações HTTP para registrar e consultar acessos. Desenvolvida em Java com Spring Boot e Spring Data MongoDB, armazena usuário, perfil, horários de entrada e saída, endereço IP e status na coleção `access_log`.

## Como funciona

- **Controller:** recebe requisições em `/api/v1/access-logs`.
- **Service:** concentra as operações sobre os registros.
- **Repository:** acessa o MongoDB por Spring Data.
- **DTO:** representa os dados trocados com o cliente.

```text
Cliente HTTP → controller → service → repository → MongoDB
```

## Início rápido

Requisitos: JDK 21 e MongoDB acessível. O Maven Wrapper está incluído.

```bash
git clone https://github.com/ApolloOficial/Access-Log-Api.git
cd Access-Log-Api
```

Configure a conexão MongoDB para o seu ambiente. Exemplo com Spring Boot 4 no PowerShell:

```powershell
$env:SPRING_MONGODB_URI = "mongodb://localhost:27017/apollo_access"
$env:SERVER_PORT = "8081"
.\mvnw.cmd spring-boot:run
```

No Linux/macOS, utilize `./mvnw`. A porta 8081 do exemplo evita conflito com a API principal. A configuração versionada contém apenas o nome da aplicação; configure as credenciais externamente.

## API

| Método | Rota | Operação |
|---|---|---|
| GET | `/api/v1/access-logs` | Listar registros |
| GET | `/api/v1/access-logs/{id}` | Consultar registro |
| POST | `/api/v1/access-logs` | Criar registro |
| DELETE | `/api/v1/access-logs/{id}` | Excluir registro |

Os registros contêm informações pessoais. Use dados fictícios em exemplos e evidências. A integração efetiva com os demais componentes deve ser validada no ambiente de execução.

## Testes

```powershell
.\mvnw.cmd test
```

Valide também a criação, consulta e exclusão de um registro fictício com o MongoDB disponível.

## Documentação

- [Endpoints](src/main/java/org/apollo/log/api/controller/AccessLogController.java)
- [Modelo persistido](src/main/java/org/apollo/log/api/model/AccessLog.java)
- [Dependências](pom.xml)

