package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class InventoryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By qualquerBotaoAdd = By.cssSelector("[data-test^='add-to-cart']");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    private By botaoAddProduto(String nomeProduto) {
        String slug = nomeProduto.toLowerCase().replace(" ", "-");
        return By.cssSelector("[data-test='add-to-cart-" + slug + "']");
    }

    public void adicionarProdutoAoCarrinho(String nomeProduto) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(qualquerBotaoAdd));
        wait.until(ExpectedConditions.elementToBeClickable(botaoAddProduto(nomeProduto)))
                .click();
        // Aguarda o botão virar "Remove" confirmando que o produto foi adicionado
        String slugRemove = nomeProduto.toLowerCase().replace(" ", "-");
        By botaoRemove = By.cssSelector("[data-test='remove-" + slugRemove + "']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(botaoRemove));
    }

    public CartPage irParaCarrinho() {
        // Navega diretamente — evita problemas de click em headless com <a> tags
        driver.navigate().to("https://www.saucedemo.com/cart.html");
        wait.until(ExpectedConditions.urlContains("cart.html"));
        return new CartPage(driver);
    }
}