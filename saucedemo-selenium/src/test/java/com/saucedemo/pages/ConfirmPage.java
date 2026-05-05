package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ConfirmPage {
    private final WebDriver driver;
    private final By mensagemSucesso = By.cssSelector(".complete-header");

    public ConfirmPage(WebDriver driver) {
        this.driver = driver;
    }

    public String obterMensagemSucesso() {
        return driver.findElement(mensagemSucesso).getText();
    }

    public boolean compraFinalizada() {
        return obterMensagemSucesso().contains("thank you for  your order!");
    }
}
