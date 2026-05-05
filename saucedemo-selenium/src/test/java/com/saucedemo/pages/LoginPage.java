package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage {
    private final WebDriver driver;
    private final By campoUsuario = By.id("user-name");
    private final By campoSenha = By.id("password");
    private final By botaoLogin = By.id("login-button");
    private final By mensagemErro = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
    }
    public void abrirPagina(){
        driver.get("https://www.saucedemo.com/");
    }
    public void digitarUsuario(String usuario){
        driver.findElement(campoUsuario).clear();
        driver.findElement(campoUsuario).sendKeys(usuario);
    }
    public void digitarSenha(String senha){
        driver.findElement(campoSenha).sendKeys(senha);
    }
    public void clicarLogin(){
        driver.findElement(botaoLogin).click();
    }
    public InventoryPage FazerLogin(String usuario , String senha){
        digitarUsuario(usuario);
        digitarSenha(senha);
        clicarLogin();
        return new InventoryPage(driver);

    }
    public String obterMensagemErro(){
        return driver.findElement(mensagemErro).getText();
    }
}
