# Desafio Técnico — API de Vendas

API REST desenvolvida com Spring Boot, Java e PostgreSQL para cadastro e edição de vendas.

---

## O que essa API faz?

Basicamente ela permite criar e atualizar vendas com seus itens através de um único endpoint. Se você mandar um `id` no body, ela atualiza a venda existente. Se não mandar, ela cria uma nova.

Além do básico do desafio, implementei dois diferenciais:

- **Auditoria automática** — cada registro guarda quem criou, quem editou e quando, sem precisar fazer nada manual
- **Autenticação em dois fatores (2FA)** — além do Basic Auth, toda requisição precisa de um código OTP de 6 dígitos no header. O código muda a cada 30 segundos

---

## Pré-requisitos

Você vai precisar ter instalado:

- **Java 17**
- **Maven** (ou usar o `./mvnw` que já vem no projeto)
- **Docker e Docker Compose** (para subir o banco)

---

## Como rodar localmente

**1. Clone o projeto**

```bash
git clone https://github.com/yasmjm/desafio-vendas-api.git
cd desafio-vendas-api
```

**2. Suba o banco de dados**

```bash
docker-compose up -d
```

Isso vai subir o PostgreSQL na porta `5433` com as seguintes configurações:

| Configuração | Valor       |
|--------------|-------------|
| Banco         | vendas_db   |
| Usuário       | postgres    |
| Senha         | postgres123 |
| Porta         | 5433        |

**3. Suba a API**

```bash
./mvnw spring-boot:run
```

A aplicação vai subir em `http://localhost:8080` e o próprio Hibernate cria as tabelas automaticamente na primeira vez.

---

## Autenticação

Todas as chamadas para `/api/**` exigem **duas coisas**:

### 1. Basic Auth

| Campo  | Valor    |
|--------|----------|
| Usuário | `admin`  |
| Senha   | `admin123` |

Em Base64 (para usar direto no header): `YWRtaW46YWRtaW4xMjM=`

```
Authorization: Basic YWRtaW46YWRtaW4xMjM=
```

### 2. Código OTP (2FA)

Antes de fazer qualquer chamada, pegue o código atual aqui:

```
GET http://localhost:8080/otp/atual
```

Resposta:
```json
{
  "codigo": "482031",
  "instrucao": "Use este código no header X-OTP-Code. Válido por 30 segundos.",
  "aviso": "Este endpoint existe apenas para fins de demonstração."
}
```

Então adicione o código no header da sua requisição:

```
X-OTP-Code: 482031
```

> O código muda a cada 30 segundos, então se a requisição retornar 401, é só buscar um novo em `/otp/atual`.

---

## Endpoints

### `POST /api/vendas` — Criar ou atualizar venda

- **Com `id` no body** → atualiza a venda existente
- **Sem `id` no body** → cria uma nova venda

**Exemplo de criação (sem `id`):**

```json
{
  "cliente": "João da Silva",
  "dataVenda": "2026-05-18",
  "valorTotal": 250.00,
  "status": "ABERTA",
  "formaPagamento": "PIX",
  "observacao": "Venda realizada no balcão",
  "itens": [
    {
      "produtoId": 1001,
      "descricaoProduto": "Ração para peixe 25kg",
      "quantidade": 2,
      "valorUnitario": 100.00,
      "valorTotal": 200.00
    },
    {
      "produtoId": 1002,
      "descricaoProduto": "Suplemento alimentar",
      "quantidade": 1,
      "valorUnitario": 50.00,
      "valorTotal": 50.00
    }
  ]
}
```

**Resposta de sucesso (HTTP 200):**

```json
{
  "id": 1,
  "cliente": "João da Silva",
  "dataVenda": "2026-05-18",
  "valorTotal": 250.00,
  "status": "ABERTA",
  "formaPagamento": "PIX",
  "observacao": "Venda realizada no balcão",
  "itens": [
    {
      "id": 1,
      "produtoId": 1001,
      "descricaoProduto": "Ração para peixe 25kg",
      "quantidade": 2,
      "valorUnitario": 100.00,
      "valorTotal": 200.00
    },
    {
      "id": 2,
      "produtoId": 1002,
      "descricaoProduto": "Suplemento alimentar",
      "quantidade": 1,
      "valorUnitario": 50.00,
      "valorTotal": 50.00
    }
  ]
}
```

**Resposta de erro de validação (HTTP 400):**

```json
{
  "status": 400,
  "erro": "Erro de validação",
  "mensagem": "Existem campos inválidos na requisição.",
  "campos": [
    {
      "campo": "cliente",
      "mensagem": "O cliente é obrigatório."
    },
    {
      "campo": "itens[0].quantidade",
      "mensagem": "A quantidade deve ser maior que zero."
    }
  ]
}
```

### `GET /otp/atual` — Buscar código 2FA atual

Sem autenticação. Retorna o código OTP vigente para usar nas chamadas autenticadas.

---

## Estrutura do projeto

```
src/
├── config/
│   ├── AuditoriaConfig.java   # captura o usuário logado para auditoria
│   ├── SecurityConfig.java    # Basic Auth + filtro 2FA
│   └── TotpFilter.java        # valida o header X-OTP-Code
├── controller/
│   ├── OtpController.java     # endpoint /otp/atual
│   └── VendaController.java   # endpoint /api/vendas
├── dto/                       # objetos de entrada e saída da API
├── entity/
│   ├── Venda.java             # tabela venda com campos de auditoria
│   └── VendaItem.java         # tabela venda_item com campos de auditoria
├── exception/
│   └── GlobalExceptionHandler.java  # trata erros e formata o JSON de retorno
├── repository/                # interfaces JPA
└── service/
    ├── TotpService.java       # gera e valida os códigos OTP
    └── VendaService.java      # regras de negócio de criar/atualizar venda
```

---

## Sobre o 2FA implementado

O sistema usa uma lógica de TOTP (Time-based One-Time Password) simplificada: o código é gerado a partir de um segredo fixo combinado com o intervalo de tempo atual (janelas de 30 segundos). Não usa nenhuma biblioteca externa — foi feito na mão para fins de demonstração.

Em um sistema real, o segredo seria por usuário, armazenado no banco de dados, e o cliente usaria um app como Google Authenticator ou Authy para gerar os códigos.
