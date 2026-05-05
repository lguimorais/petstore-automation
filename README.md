
# 🐾 Petstore API Automation

Projeto de automação de testes da [Swagger Petstore API](https://petstore.swagger.io/v2) utilizando **Postman**, **Newman** e **GitHub Actions**.

---

## 📋 Sobre o Projeto

Este repositório contém a cobertura completa dos endpoints da API pública Swagger Petstore, organizada em três módulos:

- **Pet** — gerenciamento de pets (criar, buscar, atualizar, deletar, listar por status)
- **Store** — gerenciamento de pedidos e inventário
- **User** — gerenciamento de usuários (criar, autenticar, atualizar, deletar)

Os testes são executados via **Newman** (CLI do Postman) e a pipeline roda automaticamente via **GitHub Actions** a cada push ou de forma manual pelo `workflow_dispatch`.

---

## 🗂️ Estrutura do Projeto

```
petstore-automation/
├── collections/
│   └── Petstore_v2.postman_collection.json     # Collection com todos os requests e testes
├── environments/
│   └── Petstore_Env.postman_environment.json   # Variáveis de ambiente
├── .github/
│   └── workflows/
│       └── api-tests.yml                        # Pipeline de CI com GitHub Actions
├── reports/                                     # Relatórios HTML gerados pelo Newman
└── README.md
```

---

## ⚙️ Tecnologias Utilizadas

| Ferramenta | Versão | Finalidade |
|---|---|---|
| Postman | Latest | Criação e organização dos requests |
| Newman | 6.x | Execução da collection via CLI |
| newman-reporter-htmlextra | Latest | Geração de relatórios HTML |
| GitHub Actions | — | Pipeline de CI/CD |
| Node.js | 20 | Runtime para o Newman |

---

## 🌐 Base URL

```
https://petstore.swagger.io/v2
```

---

## 🔑 Variáveis de Ambiente

| Variável | Valor | Descrição |
|---|---|---|
| `base_url` | `https://petstore.swagger.io/v2` | URL base da API |
| `pet_id` | _(dinâmico)_ | ID do pet capturado via script |
| `order_id` | _(dinâmico)_ | ID do pedido capturado via script |
| `username` | _(dinâmico)_ | Username capturado via script |

> As variáveis dinâmicas são preenchidas automaticamente pelos scripts `pm.environment.set()` durante a execução dos testes.

---

## 🧪 Endpoints Cobertos

### Pet `/pet`
| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/pet` | Criar novo pet |
| GET | `/pet/{petId}` | Buscar pet por ID |
| PUT | `/pet` | Atualizar pet |
| DELETE | `/pet/{petId}` | Deletar pet |
| GET | `/pet/findByStatus` | Listar pets por status |
| POST | `/pet/{petId}/uploadImage` | Upload de imagem |

### Store `/store`
| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/store/inventory` | Consultar inventário |
| POST | `/store/order` | Criar pedido |
| GET | `/store/order/{orderId}` | Buscar pedido por ID |
| DELETE | `/store/order/{orderId}` | Deletar pedido |

### User `/user`
| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/user` | Criar usuário |
| GET | `/user/{username}` | Buscar usuário |
| PUT | `/user/{username}` | Atualizar usuário |
| DELETE | `/user/{username}` | Deletar usuário |
| GET | `/user/login` | Login |
| GET | `/user/logout` | Logout |

---

## 🚀 Como Executar Localmente

### Pré-requisitos

- [Node.js 20+](https://nodejs.org/)
- [Newman](https://www.npmjs.com/package/newman)
- [newman-reporter-htmlextra](https://www.npmjs.com/package/newman-reporter-htmlextra)

### Instalação

```bash
npm install -g newman
npm install -g newman-reporter-htmlextra
```

### Rodar todos os testes

```bash
newman run collections/Petstore_v2.postman_collection.json \
  -e environments/Petstore_Env.postman_environment.json \
  --reporters cli,htmlextra \
  --reporter-htmlextra-export reports/result.html
```

### Rodar por pasta específica

```bash
# Apenas Pet
newman run collections/Petstore_v2.postman_collection.json \
  -e environments/Petstore_Env.postman_environment.json \
  --folder "Pet"

# Apenas Store
newman run collections/Petstore_v2.postman_collection.json \
  -e environments/Petstore_Env.postman_environment.json \
  --folder "Store"

# Apenas User
newman run collections/Petstore_v2.postman_collection.json \
  -e environments/Petstore_Env.postman_environment.json \
  --folder "User"
```

---

## 🔄 Pipeline CI — GitHub Actions

A pipeline executa os testes automaticamente e gera relatórios HTML separados por módulo.

### Trigger

- **Manual** via `workflow_dispatch` na aba Actions do GitHub

### Etapas

1. Checkout do repositório
2. Instalação do Node.js 20
3. Instalação do Newman e reporter HTML
4. Injeção de variáveis sensíveis via secrets
5. Execução dos testes por pasta (Pet, Store, User)
6. Upload dos relatórios HTML como artefatos

### Como executar manualmente

1. Acesse a aba **Actions** no repositório
2. Selecione o workflow **Petstore API Tests**
3. Clique em **Run workflow**
4. Selecione o ambiente e confirme

### Relatórios

Após a execução, os relatórios ficam disponíveis em **Actions → Run → Artifacts** com o nome:

```
relatorios-petstore-{run_number}
```

Cada módulo gera um relatório separado:
- `report-pet.html`
- `report-store.html`
- `report-user.html`

---

## 🔐 Secrets Necessários

Configure os seguintes secrets em **Settings → Secrets and variables → Actions**:

| Secret | Descrição |
|---|---|
| `PETSTORE_API_KEY` | API key da Petstore (`special-key` para ambiente de testes) |

---

## 📄 Licença

Este projeto foi desenvolvido para fins acadêmicos na disciplina de **Teste e Qualidade de Software**.
