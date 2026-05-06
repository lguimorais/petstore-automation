package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class InventoryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Actions actions;

    private final By qualquerBotaoAdd = By.cssSelector("[data-test^='add-to-cart']");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        this.actions = new Actions(driver);
    }

    private By botaoAddProduto(String nomeProduto) {
        String slug = nomeProduto.toLowerCase().replace(" ", "-");
        return By.cssSelector("[data-test='add-to-cart-" + slug + "']");
    }

    public void adicionarProdutoAoCarrinho(String nomeProduto) {
        // Aguarda a página estar pronta
        wait.until(ExpectedConditions.visibilityOfElementLocated(qualquerBotaoAdd));

        WebElement botao = wait.until(
                ExpectedConditions.elementToBeClickable(botaoAddProduto(nomeProduto))
        );

        // Scroll até o botão + click via Actions (mais confiável em headless)
        actions.scrollToElement(botao).perform();
        actions.click(botao).perform();
    }

    public CartPage irParaCarrinho() {
        driver.navigate().to("https://www.saucedemo.com/cart.html");
        wait.until(ExpectedConditions.urlContains("cart.html"));
        return new CartPage(driver);
    }
}