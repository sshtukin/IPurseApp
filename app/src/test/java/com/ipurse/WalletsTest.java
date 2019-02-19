package com.ipurse;

import com.ipurse.models.ipurse.Wallet;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class WalletsTest {
    private Wallet mWallet;

    @Before
    public void setUp() throws Exception {
        mWallet = new Wallet();
        mWallet.setName("Test wallet");
        mWallet.setSum(100500);
        mWallet.setCurrency("USD");
    }

    @Test
    public void testWalletName() {
        assertThat(mWallet.getName(), is("Test wallet"));
    }

    @Test
    public void testWalletWrongName() {
        assertNotEquals(mWallet.getName(), is("wrongName"));
    }

    @Test
    public void testWalletSum() {
        assertThat(mWallet.getSum(), is(100500.0));
    }

    @Test
    public void testWalletWrongSum() {
        assertNotEquals(mWallet.getSum(), is(100500.0));
    }

    @Test
    public void testWalletCurrency() {
        assertThat(mWallet.getCurrency(), is("USD"));
    }

    @Test
    public void testWalletWrongCurrency() {
        assertNotEquals(mWallet.getCurrency(), is("WrongCurrency"));
    }
}