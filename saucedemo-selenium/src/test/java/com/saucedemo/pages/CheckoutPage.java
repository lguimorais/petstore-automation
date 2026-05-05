package com.saucedemo.pages;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.By;


public class CheckoutPage {
    private final WebDriver driver;
    private final By campoNome = By.id("first-name");
    private final By campoSobrenome = By.id("last-name");
    private final By campoCep = By.id("postal-code");
    private final By botaoContinuar = By.id("continue");
    private final By botaoFinalizar = By.id("finish");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
    }
    public void preencherInformacoes(String nome, String sobrenome, String cep){
        driver.findElement(campoNome).sendKeys(nome);
        driver.findElement(campoSobrenome).sendKeys(sobrenome);
        driver.findElement(campoCep).sendKeys(cep);

    }
    public void clicarContunuar(){
        driver.findElement(botaoContinuar).click();
    }
    public ConfirmPage finalizarCompra(){
        driver.findElement(botaoFinalizar).click();
        return new ConfirmPage(driver);
    }
}
