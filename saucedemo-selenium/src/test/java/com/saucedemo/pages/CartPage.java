package com.saucedemo.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public CheckoutPage irParaCheckout() {
        // Navega direto — mesma estratégia que funcionou para o carrinho
        driver.navigate().to("https://www.saucedemo.com/checkout-step-one.html");
        wait.until(ExpectedConditions.urlContains("checkout-step-one"));
        return new CheckoutPage(driver);
    }
}