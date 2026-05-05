package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InventoryPage {
    private final WebDriver driver;
    private final By iconeCarrinho = By.id("shopping_cart_container");



    private By botaoAddProduto(String nomeProduto) {
        String seletor = String.format("[data-test='add-to-cart-%s']", nomeProduto.toLowerCase().replace(" ", "-"));
        return By.cssSelector(seletor);
        }

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
    }
    public void adicionarProdutoAoCarrinho(String nomeProduto){
        driver.findElement(botaoAddProduto(nomeProduto)).click();
    }
    public CartPage irParaCarrinho(){
        driver.findElement(iconeCarrinho).click();
        return new CartPage(driver);
    }

}
