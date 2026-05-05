package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class CartPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By botaoCheckout = By.id("checkout");

    public CartPage(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(40));
        this.driver = driver;
    }

    public CheckoutPage irParaCheckout() {
        wait.until(ExpectedConditions.elementToBeClickable(botaoCheckout))
                .click();
        return new CheckoutPage(driver);
    }

}
