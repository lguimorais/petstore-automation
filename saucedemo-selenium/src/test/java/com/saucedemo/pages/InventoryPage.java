package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class InventoryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By iconeCarrinho = By.id("shopping_cart_container");
    // Aguarda qualquer botão "Add to cart" para confirmar que a página carregou
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
        // Espera a página de produtos carregar completamente antes de qualquer ação
        wait.until(ExpectedConditions.visibilityOfElementLocated(qualquerBotaoAdd));
        // Espera o botão específico deste produto estar clicável
        wait.until(ExpectedConditions.elementToBeClickable(botaoAddProduto(nomeProduto)))
                .click();
    }

    public CartPage irParaCarrinho() {
        wait.until(ExpectedConditions.elementToBeClickable(iconeCarrinho)).click();
        return new CartPage(driver);
    }
}