# 🤖 SauceDemo — Automação E2E com Selenium + Java

![Java](https://img.shields.io/badge/Java-17+-orange?style=flat-square&logo=openjdk)
![Selenium](https://img.shields.io/badge/Selenium-4.18.1-43B02A?style=flat-square&logo=selenium)
![TestNG](https://img.shields.io/badge/TestNG-7.9.0-red?style=flat-square)
![Maven](https://img.shields.io/badge/Maven-3.8+-C71A36?style=flat-square&logo=apachemaven)
![CI](https://img.shields.io/badge/CI-GitHub%20Actions-2088FF?style=flat-square&logo=githubactions)

Projeto de automação de testes **End-to-End (E2E)** para o site [SauceDemo](https://www.saucedemo.com/), cobrindo o fluxo completo de compra: login → seleção de produtos → carrinho → checkout → confirmação de pedido.

---

## 📋 Índice

- [Visão Geral](#-visão-geral)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Pré-requisitos](#-pré-requisitos)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Como Executar](#-como-executar)
- [Como Funciona](#-como-funciona)
- [Decisões Técnicas](#-decisões-técnicas)
- [Pipeline CI/CD](#-pipeline-cicd)
- [Credenciais de Teste](#-credenciais-de-teste)

---

## 🎯 Visão Geral

Este projeto automatiza o fluxo de compra do SauceDemo de ponta a ponta, validando que todas as etapas funcionam corretamente. Os testes rodam em modo **headless** (sem abrir janela do browser), tanto localmente quanto na pipeline de CI do GitHub Actions.

### Fluxo automatizado

```
1. Abrir https://www.saucedemo.com/
2. Fazer login com standard_user / secret_sauce
3. Adicionar dois produtos ao carrinho (Backpack + Bike Light)
4. Navegar para o carrinho
5. Iniciar o checkout
6. Preencher dados pessoais (nome, sobrenome, CEP)
7. Confirmar o resumo do pedido
8. Finalizar a compra
9. Validar mensagem "Thank you for your order!"
```

---

## 🛠 Tecnologias Utilizadas

| Tecnologia | Versão | Função |
|---|---|---|
| Java | 17+ | Linguagem principal |
| Maven | 3.8+ | Gerenciamento de dependências e build |
| Selenium WebDriver | 4.18.1 | Automação do browser |
| TestNG | 7.9.0 | Framework de testes |
| WebDriverManager | 5.7.0 | Download automático do ChromeDriver |
| GitHub Actions | — | Pipeline de integração contínua (CI) |

---

## ✅ Pré-requisitos

### Java 17+
```bash
java -version
```
Baixe em [adoptium.net](https://adoptium.net/) se necessário.

### Maven 3.8+
```bash
mvn -version
```
Baixe em [maven.apache.org](https://maven.apache.org/download.cgi) se necessário.

### Google Chrome
Qualquer versão moderna. O **WebDriverManager** detecta a versão instalada e baixa o ChromeDriver correspondente automaticamente — sem configuração manual.

---

## 📁 Estrutura do Projeto

```
petstore-automation/
├── .github/
│   └── workflows/
│       └── ci.yml                        # Pipeline GitHub Actions
└── saucedemo-selenium/
    ├── src/
    │   └── test/
    │       └── java/
    │           └── com/saucedemo/
    │               ├── base/
    │               │   └── BaseTest.java         # Setup/teardown do WebDriver
    │               ├── pages/
    │               │   ├── LoginPage.java        # Tela de login
    │               │   ├── InventoryPage.java    # Listagem de produtos
    │               │   ├── CartPage.java         # Carrinho de compras
    │               │   ├── CheckoutPage.java     # Formulário de checkout
    │               │   └── ConfirmPage.java      # Confirmação do pedido
    │               └── tests/
    │                   └── CompraE2ETest.java    # Teste ponta a ponta
    └── pom.xml                           # Dependências Maven
```

---

## ▶ Como Executar

### Clonar o repositório
```bash
git clone https://github.com/lguimorais/petstore-automation.git
cd petstore-automation/saucedemo-selenium
```

### Rodar todos os testes
```bash
mvn test
```

### Rodar uma classe específica
```bash
mvn test -Dtest=CompraE2ETest
```

### Rodar um método específico
```bash
mvn test -Dtest=CompraE2ETest#deveFinalizarCompraCom2Produtos
```

### Ver o browser abrindo (modo visual — útil para depuração)

No `BaseTest.java`, comente as linhas de headless:

```java
// options.addArguments("--headless=new");
// options.addArguments("--no-sandbox");
// options.addArguments("--disable-dev-shm-usage");
```

> ⚠️ Lembre de descomentar antes de fazer commit — o CI exige modo headless.

### Resultado esperado

```
-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running TestSuite
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

BUILD SUCCESS
```

### Relatório de testes

Após a execução, os relatórios ficam em:
```
target/surefire-reports/
```

---

## ⚙ Como Funciona

### Padrão Page Object Model (POM)

O projeto separa **onde os elementos estão** (Page Objects) da **lógica dos testes**. Cada tela do SauceDemo vira uma classe Java. Cada Page Object retorna o próximo ao final da sua ação, criando um fluxo encadeado e legível:

```java
InventoryPage inventario = login.fazerLogin("standard_user", "secret_sauce");
inventario.adicionarProdutoAoCarrinho("Sauce Labs Backpack");
CartPage carrinho = inventario.irParaCarrinho();
CheckoutPage checkout = carrinho.irParaCheckout();
```

---

### BaseTest.java — Ciclo de vida do WebDriver

| Anotação | Quando executa | O que faz |
|---|---|---|
| `@BeforeMethod` | Antes de cada `@Test` | Abre Chrome headless via WebDriverManager |
| `@AfterMethod` | Depois de cada `@Test` | Encerra o browser com `driver.quit()` |

O `ThreadLocal<WebDriver>` garante que testes paralelos não compartilhem o mesmo driver.

---

### InventoryPage.java

- Aguarda a página carregar com `visibilityOfElementLocated` antes de qualquer ação
- Usa `Actions.scrollToElement().click()` para adicionar produtos — mais confiável em headless
- O slug do produto é gerado automaticamente: `"Sauce Labs Backpack"` → `add-to-cart-sauce-labs-backpack`
- Navega ao carrinho com `driver.navigate().to()` para garantir a navegação em qualquer ambiente

---

### CartPage.java

- Aguarda `elementToBeClickable` no botão "Checkout"
- Confirma `urlContains("checkout-step-one")` antes de retornar o `CheckoutPage`

---

### CheckoutPage.java

- `clicarEDigitar()`: faz `click()` + `clear()` + `sendKeys()` — o `click()` dá foco real ao campo para o React registrar o input corretamente
- `clicarContinuar()` confirma `urlContains("checkout-step-two")` após o clique

---

### Garantia de navegação — regra do projeto

Cada método que navega confirma a URL **antes** de retornar o próximo Page Object:

```
LoginPage.fazerLogin()          → confirma inventory.html → retorna InventoryPage
InventoryPage.irParaCarrinho()  → confirma cart.html      → retorna CartPage
CartPage.irParaCheckout()       → confirma step-one       → retorna CheckoutPage
CheckoutPage.clicarContinuar()  → confirma step-two
ConfirmPage.compraFinalizada()  → confirma checkout-complete
```

Isso elimina condições de corrida entre páginas.

---

### Esperas explícitas

O projeto usa **apenas esperas explícitas** (`WebDriverWait`). `Thread.sleep()` não é usado em lugar algum.

| Condição | Quando usar |
|---|---|
| `urlContains("...")` | Confirmar navegação entre páginas |
| `visibilityOfElementLocated(...)` | Aguardar elemento visível na tela |
| `elementToBeClickable(...)` | Aguardar elemento habilitado antes de interagir |

---

## 🧠 Decisões Técnicas

### Por que `Actions.click()` em vez de `WebElement.click()`?

O Chrome headless em ambientes CI (sem GPU, sem interface gráfica) às vezes ignora cliques em elementos fora do viewport. O `Actions.scrollToElement()` garante que o elemento está visível antes do clique, tornando a automação mais estável em qualquer ambiente.

### Por que `driver.navigate().to()` para ir ao carrinho?

O ícone do carrinho no SauceDemo é um `<div>` pai que contém um `<a href="/cart.html">`. Em modo headless, clicar no `<div>` nem sempre propaga o evento para o link filho. Navegar diretamente pela URL é equivalente ao comportamento do usuário e funciona de forma idêntica em todos os ambientes.

### Por que `click()` antes de `sendKeys()` no checkout?

O SauceDemo é construído em React. Para que o React registre a digitação, o campo precisa ter foco real — não apenas estar visível no DOM. O `click()` explícito antes do `sendKeys()` estabelece esse foco.

### Por que o CI não usa `browser-actions/setup-chrome`?

Instalar uma versão específica do Chromium via `setup-chrome` criava incompatibilidade com o ChromeDriver que o WebDriverManager baixava. Sem o `setup-chrome`, o WebDriverManager detecta a versão do Chrome já disponível no Ubuntu da GitHub Actions e baixa o ChromeDriver exato para aquela versão — sem conflito.

---

## 🔄 Pipeline CI/CD

O projeto está integrado ao **GitHub Actions**. A pipeline roda automaticamente em cada push para `main` ou `develop`, e em pull requests para `main`.

### Etapas da pipeline

```
1. Checkout do código
2. Configurar Java 17 (Temurin)
3. Cache das dependências Maven (~/.m2)
4. Executar mvn test (WebDriverManager gerencia o Chrome automaticamente)
5. Publicar relatório TestNG como artifact (mesmo em caso de falha)
```

### Como ver os resultados

1. Acesse o repositório no GitHub
2. Clique na aba **Actions**
3. Clique no run mais recente
4. Em caso de falha, baixe o relatório em **Artifacts → relatorio-testes**

---

## 🔑 Credenciais de Teste

| Usuário | Senha | Comportamento |
|---|---|---|
| `standard_user` | `secret_sauce` | ✅ Fluxo completo funciona normalmente |
| `locked_out_user` | `secret_sauce` | ❌ Bloqueado — exibe mensagem de erro |
| `problem_user` | `secret_sauce` | ⚠️ Imagens quebradas e bugs intencionais |
| `performance_glitch_user` | `secret_sauce` | 🐢 Login com latência simulada |

> Os testes utilizam `standard_user` para o fluxo de compra bem-sucedido.

---

## 📝 Sobre o Projeto

Desenvolvido para a disciplina de **Teste e Qualidade de Software** como exercício prático de automação E2E com Selenium WebDriver, aplicando o padrão Page Object Model e integração com pipeline CI/CD via GitHub Actions.
