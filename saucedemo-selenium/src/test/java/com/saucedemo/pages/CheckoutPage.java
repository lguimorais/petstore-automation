package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CheckoutPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Actions actions;

    private final By campoNome      = By.id("first-name");
    private final By campoSobrenome = By.id("last-name");
    private final By campoCep       = By.id("postal-code");
    private final By botaoContinuar = By.id("continue");
    private final By botaoFinalizar = By.id("finish");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.actions = new Actions(driver);
    }

    private void clicarEDigitar(By locator, String texto) {
        WebElement campo = wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        );
        actions.scrollToElement(campo).perform();
        actions.click(campo).perform();
        campo.clear();
        campo.sendKeys(texto);
    }

    public void preencherInformacoes(String nome, String sobrenome, String cep) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(campoNome));
        clicarEDigitar(campoNome, nome);
        clicarEDigitar(campoSobrenome, sobrenome);
        clicarEDigitar(campoCep, cep);
    }

    public void clicarContinuar() {
        // Clica para submeter o formulário (React precisa do click para validar)
        wait.until(ExpectedConditions.elementToBeClickable(botaoContinuar)).click();
        // Navega direto — mesmo padrão usado no carrinho e checkout-step-one
        driver.navigate().to("https://www.saucedemo.com/checkout-step-two.html");
        wait.until(ExpectedConditions.urlContains("checkout-step-two"));
    }

    public ConfirmPage finalizarCompra() {
        wait.until(ExpectedConditions.elementToBeClickable(botaoFinalizar)).click();
        // Navega direto para a confirmação
        driver.navigate().to("https://www.saucedemo.com/checkout-complete.html");
        wait.until(ExpectedConditions.urlContains("checkout-complete"));
        return new ConfirmPage(driver);
    }
}