package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ConfirmPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final By mensagemSucesso = By.cssSelector(".complete-header");

    public ConfirmPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(40));
    }

    public String obterMensagemSucesso() {
        wait.until(ExpectedConditions.urlContains("checkout-complete"));
        return wait.until(ExpectedConditions
                        .visibilityOfElementLocated(mensagemSucesso))
                .getText();
    }

    public boolean compraFinalizada() {
        return obterMensagemSucesso().contains("Thank you for your order!");
    }
}