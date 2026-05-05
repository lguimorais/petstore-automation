package com.saucedemo.tests;
import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CompraE2ETest extends BaseTest {
    @Test
    public void testarLogin() {
        LoginPage login = new LoginPage(getDriver());
        login.abrirPagina();
        login.fazerLogin("standard_user", "secret_sauce");

        // Valida que saiu da tela de login (URL mudou)
        String urlAtual = getDriver().getCurrentUrl();
        Assert.assertTrue(urlAtual.contains("inventory"),
                "Login falhou! URL atual: " + urlAtual);

        System.out.println("✅ Login OK — URL: " + urlAtual);
    }
}