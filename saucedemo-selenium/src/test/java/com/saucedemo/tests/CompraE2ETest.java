package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CompraE2ETest extends BaseTest {
    @Test(description = "fluxo completo: Login ,carrinho e compra ")
    public void deveFinalizarCompraCom2Produtos() {
        LoginPage login = new LoginPage(getDriver());
        login.abrirPagina();
        InventoryPage inentario = login.fazerLogin("standard_user", "secret_sauce");
        inentario.adicionarProdutoAoCarrinho("Sauce labs Backpack");
        inentario.adicionarProdutoAoCarrinho("Sauce Labs Bike Light");

        CartPage carrinho = inentario.irParaCarrinho();

        CheckoutPage checkout = carrinho.irParaCheckout();

        checkout.preencherInformacoes("luis", "Guilherme", "64093000");
        checkout.clicarContinuar();
        ConfirmPage confimacao = checkout.finalizarCompra();
        Assert.assertTrue(confimacao.compraFinalizada(), "A mensagem de cofirmação não apareceu");

    }


}