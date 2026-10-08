# Access-Log-Api

API REST (Java 21 / Spring Boot 4 / Spring MVC) que registra acessos (login/logout) em **MongoDB** (NoSQL).
Corresponde a demanda DS-10 do Jira. Os campos espelham a tabela `access_log` do PostgreSQL do Apollo-API-Spring.

## Configuracao

| Variavel | Descricao | Padrao |
|---|---|---|
| `MONGODB_URI` | URI do MongoDB | `mongodb://localhost:27017/apollo_access_log` |
| `ACCESS_LOG_API_KEY` | Chave exigida no header `X-API-Key` | (vazia: tudo responde 401) |
| `PORT` | Porta HTTP | `8081` |

## Endpoints (`/api/v1/access-logs`, header `X-API-Key`)

| Metodo | Rota | Descricao |
|---|---|---|
| GET | `/` | Lista paginada. Filtros: `email`, `status` (SUCESSO/FALHA), `from`, `to` (ISO-8601), `page`, `size`, `sortBy`, `direction` |
| GET | `/{id}` | Busca por id |
| POST | `/` | Registra um acesso (`sessionId` opcional, gerado se ausente; SUCESSO exige `employeeId`) |
| PATCH | `/{id}/logout` | Registra o logout da sessao |
| DELETE | `/{id}` | Remove um registro |

Swagger: `/swagger-ui/index.html`. Erros seguem o formato `{ "status": 400, "message": "..." }`, igual ao Apollo-API-Spring.

## Rodando

```
docker run -d -p 27017:27017 mongo:7
set ACCESS_LOG_API_KEY=minha-chave
mvn spring-boot:run
```
