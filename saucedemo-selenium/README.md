# 🤖 SauceDemo — Automação E2E com Selenium + Java

![Java](https://img.shields.io/badge/Java-17+-orange?style=flat-square&logo=openjdk)
![Selenium](https://img.shields.io/badge/Selenium-4.18.1-43B02A?style=flat-square&logo=selenium)
![TestNG](https://img.shields.io/badge/TestNG-7.9.0-red?style=flat-square)
![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36?style=flat-square&logo=apachemaven)
![CI](https://img.shields.io/badge/CI-GitHub%20Actions-2088FF?style=flat-square&logo=githubactions)

Projeto de automação de testes **End-to-End (E2E)** para o site [SauceDemo](https://www.saucedemo.com/), cobrindo o fluxo completo de compra: login → seleção de produtos → carrinho → checkout → confirmação.

---

## 📋 Índice

- [Visão Geral](#-visão-geral)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Pré-requisitos](#-pré-requisitos)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Como Executar](#-como-executar)
- [Como Funciona](#-como-funciona)
- [Padrão Page Object Model](#-padrão-page-object-model)
- [Pipeline CI/CD](#-pipeline-cicd)
- [Credenciais de Teste](#-credenciais-de-teste)

---

## 🎯 Visão Geral

Este projeto automatiza o fluxo de compra do SauceDemo de forma ponta a ponta, validando que todas as etapas funcionam corretamente. Os testes rodam em modo **headless** (sem abrir janela do browser), tanto localmente quanto na pipeline de CI do GitHub Actions.

### Fluxo automatizado

```
1. Abrir https://www.saucedemo.com/
2. Fazer login com usuário válido
3. Adicionar dois produtos ao carrinho
4. Acessar o carrinho e iniciar o checkout
5. Preencher dados pessoais (nome, sobrenome, CEP)
6. Confirmar o pedido
7. Validar a mensagem "Thank you for your order!"
```

---

## 🛠 Tecnologias Utilizadas

| Tecnologia | Versão | Função |
|---|---|---|
| Java | 17+ | Linguagem principal |
| Maven | 3.8+ | Gerenciamento de dependências e build |
| Selenium WebDriver | 4.18.1 | Automação do browser |
| TestNG | 7.9.0 | Framework de testes (anotações, assertions, relatórios) |
| WebDriverManager | 5.7.0 | Download automático do ChromeDriver |
| GitHub Actions | — | Pipeline de integração contínua (CI) |

---

## ✅ Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

### 1. Java 17 ou superior

Verifique com:
```bash
java -version
```

Caso não tenha, baixe em [adoptium.net](https://adoptium.net/) e configure a variável `JAVA_HOME` no seu sistema.

### 2. Maven 3.8 ou superior

Verifique com:
```bash
mvn -version
```

Caso não tenha, baixe em [maven.apache.org](https://maven.apache.org/download.cgi) e adicione ao `PATH`.

### 3. Google Chrome

O projeto usa o ChromeDriver, gerenciado automaticamente pelo WebDriverManager. Qualquer versão moderna do Chrome é compatível.

### 4. Git (opcional, para clonar o repositório)

```bash
git --version
```

---

## 📁 Estrutura do Projeto

```
saucedemo-selenium/
├── .github/
│   └── workflows/
│       └── ci.yml                  # Pipeline GitHub Actions
├── src/
│   └── test/
│       └── java/
│           └── com/saucedemo/
│               ├── base/
│               │   └── BaseTest.java       # Setup e teardown do WebDriver
│               ├── pages/
│               │   ├── LoginPage.java      # Page Object da tela de login
│               │   ├── InventoryPage.java  # Page Object da listagem de produtos
│               │   ├── CartPage.java       # Page Object do carrinho
│               │   ├── CheckoutPage.java   # Page Object do formulário de checkout
│               │   └── ConfirmPage.java    # Page Object da confirmação de compra
│               └── tests/
│                   └── CompraE2ETest.java  # Testes End-to-End
├── pom.xml                         # Dependências e configuração Maven
└── README.md
```

---

## ▶ Como Executar

### Clonar o repositório

```bash
git clone https://github.com/seu-usuario/saucedemo-selenium.git
cd saucedemo-selenium
```

### Executar todos os testes

```bash
mvn test
```

### Executar uma classe específica

```bash
mvn test -Dtest=CompraE2ETest
```

### Executar um método específico

```bash
mvn test -Dtest=CompraE2ETest#deveFinalizarCompraCom2Produtos
```

### Ver o browser abrindo (desativar modo headless)

Por padrão, os testes rodam sem abrir janela do browser. Para **ver o browser** durante a execução (útil para depuração), edite o `BaseTest.java` e **comente** as linhas:

```java
// options.addArguments("--headless=new");   // ← comente esta linha
// options.addArguments("--no-sandbox");     // ← comente esta linha
// options.addArguments("--disable-dev-shm-usage"); // ← comente esta linha
```

> ⚠️ Lembre de descomentar antes de fazer commit, pois o CI exige modo headless.

### Resultado esperado após execução bem-sucedida

```
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running TestSuite
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

BUILD SUCCESS
```

### Onde encontrar o relatório de testes

Após a execução, o TestNG gera relatórios em:

```
target/surefire-reports/
```

---

## ⚙ Como Funciona

### BaseTest.java — Inicialização do WebDriver

Toda classe de teste herda de `BaseTest`. Ela é responsável por:

- **`@BeforeMethod`**: Antes de cada teste, configura e abre o Chrome em modo headless usando o `WebDriverManager` (que baixa o ChromeDriver automaticamente, sem necessidade de download manual).
- **`@AfterMethod`**: Após cada teste, encerra o browser e libera recursos com `driver.quit()`.
- **`ThreadLocal<WebDriver>`**: Permite execução paralela segura — cada thread tem seu próprio driver isolado.

```
@BeforeMethod → abre Chrome headless → executa @Test → @AfterMethod → fecha Chrome
```

### Page Object Model (POM)

O projeto separa a **lógica de localização de elementos** da **lógica dos testes**. Cada tela do SauceDemo é representada por uma classe Java:

#### LoginPage
- Localiza os campos de usuário, senha e o botão de login pelo atributo `id`
- O método `fazerLogin()` encapsula toda a sequência e retorna um `InventoryPage`

#### InventoryPage
- Localiza o botão "Add to cart" de cada produto dinamicamente pelo `data-test` attribute
- O slug do produto é gerado automaticamente: `"Sauce Labs Backpack"` → `add-to-cart-sauce-labs-backpack`
- O método `irParaCarrinho()` clica no ícone do carrinho e retorna um `CartPage`

#### CartPage
- Localiza o botão "Checkout" e aguarda ele estar clicável antes de clicar
- Retorna um `CheckoutPage` após o clique

#### CheckoutPage
- Usa `visibilityOfElementLocated` + `click()` + `sendKeys()` para preencher campos com foco real
- Aguarda a URL mudar para `checkout-step-two` após clicar em "Continue", confirmando que o React aceitou os dados
- Retorna um `ConfirmPage` após finalizar

#### ConfirmPage
- Aguarda a URL `checkout-complete` e verifica o texto `.complete-header`
- O método `compraFinalizada()` retorna `true` se a mensagem contém "Thank you for your order!"

### Fluxo de navegação entre Page Objects

```
LoginPage
    └─ fazerLogin()        → InventoryPage
         └─ irParaCarrinho()   → CartPage
              └─ irParaCheckout()  → CheckoutPage
                   └─ finalizarCompra()  → ConfirmPage
                        └─ compraFinalizada() → boolean ✅
```

### Esperas (Waits)

O projeto usa **esperas explícitas** (`WebDriverWait`) em vez de `Thread.sleep()`:

| Condição | Uso |
|---|---|
| `urlContains("...")` | Confirma que a navegação entre páginas ocorreu |
| `visibilityOfElementLocated(...)` | Aguarda o elemento aparecer na tela |
| `elementToBeClickable(...)` | Aguarda o elemento estar habilitado e visível antes de clicar |

---

## 📐 Padrão Page Object Model

O POM é um padrão de design para automação de testes que traz três benefícios principais:

**Reutilização** — A `LoginPage` pode ser usada em qualquer teste que precise autenticar, sem duplicar código.

**Manutenção** — Se o seletor de um elemento mudar no site, você altera em apenas um lugar (na Page Object), e todos os testes que a usam são corrigidos automaticamente.

**Legibilidade** — O teste lê como uma história em português:

```java
InventoryPage inventario = login.fazerLogin("standard_user", "secret_sauce");
inventario.adicionarProdutoAoCarrinho("Sauce Labs Backpack");
CartPage carrinho = inventario.irParaCarrinho();
```

---

## 🔄 Pipeline CI/CD

O projeto está integrado ao **GitHub Actions**. A pipeline é definida em `.github/workflows/ci.yml` e executa automaticamente em cada:

- `push` para as branches `main` ou `develop`
- `pull_request` aberto para `main`

### Etapas da pipeline

```
1. Checkout do código
2. Configurar Java 17 (Temurin)
3. Cache das dependências Maven (~/.m2)
4. Instalar Google Chrome
5. Executar: mvn test (headless)
6. Publicar relatório TestNG como artifact
```

### Como ver os resultados no GitHub

1. Acesse seu repositório no GitHub
2. Clique na aba **Actions**
3. Clique no run mais recente
4. Veja os logs de cada etapa em tempo real
5. Baixe o relatório em **Artifacts → relatorio-testes**

---

## 🔑 Credenciais de Teste

O SauceDemo fornece usuários de teste pré-configurados:

| Usuário | Senha | Comportamento |
|---|---|---|
| `standard_user` | `secret_sauce` | Usuário normal — fluxo completo funciona |
| `locked_out_user` | `secret_sauce` | Bloqueado — exibe mensagem de erro |
| `problem_user` | `secret_sauce` | Imagens quebradas e bugs intencionais |
| `performance_glitch_user` | `secret_sauce` | Login lento (simula latência) |

> Os testes deste projeto utilizam `standard_user` para o fluxo de compra bem-sucedido.

---

## 📝 Licença

Este projeto foi desenvolvido para fins acadêmicos na disciplina de **Teste e Qualidade de Software**.
