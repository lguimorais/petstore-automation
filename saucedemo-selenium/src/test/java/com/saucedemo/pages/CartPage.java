package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
        WebElement btn = wait.until(
                ExpectedConditions.visibilityOfElementLocated(botaoCheckout)
        );

        // força scroll até o elemento (sem JS pesado)
        new org.openqa.selenium.interactions.Actions(driver)
                .moveToElement(btn)
                .perform();
        wait.until(ExpectedConditions.elementToBeClickable(botaoCheckout))
                .click();
        wait.until(ExpectedConditions.urlContains("checkout-step-one"));
        return new CheckoutPage(driver);
    }

}
