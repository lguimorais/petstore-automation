package com.saucedemo.pages;
import  org.openqa.selenium.By;
import  org.openqa.selenium.WebDriver;

public class CartPage {
    private final WebDriver driver;
    private final By botaoCheckout = By.id("checkout");

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }
    public CheckoutPage irParaCheckout(){
        driver.findElement(botaoCheckout).click();
        return new CheckoutPage(driver);
    }

}
