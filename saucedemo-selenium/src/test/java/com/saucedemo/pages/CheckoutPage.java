package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CheckoutPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By campoNome      = By.id("first-name");
    private final By campoSobrenome = By.id("last-name");
    private final By campoCep       = By.id("postal-code");
    private final By botaoContinuar = By.id("continue");
    private final By botaoFinalizar = By.id("finish");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    private void clicarEDigitar(By locator, String texto) {
        WebElement campo = wait.until(
                ExpectedConditions.elementToBeClickable(locator)
        );
        campo.click();
        campo.clear();
        campo.sendKeys(texto);
    }

    public void preencherInformacoes(String nome, String sobrenome, String cep) {
        // Espera o primeiro campo estar pronto — garante que a página carregou
        wait.until(ExpectedConditions.elementToBeClickable(campoNome));
        clicarEDigitar(campoNome, nome);
        clicarEDigitar(campoSobrenome, sobrenome);
        clicarEDigitar(campoCep, cep);
    }

    public void clicarContinuar() {
        wait.until(ExpectedConditions.elementToBeClickable(botaoContinuar)).click();
        wait.until(ExpectedConditions.urlContains("checkout-step-two"));
    }

    public ConfirmPage finalizarCompra() {
        wait.until(ExpectedConditions.elementToBeClickable(botaoFinalizar)).click();
        return new ConfirmPage(driver);
    }
}