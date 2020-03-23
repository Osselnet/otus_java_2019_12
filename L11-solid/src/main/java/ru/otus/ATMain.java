package ru.otus;

import ru.otus.atm.*;
import ru.otus.banknotes.*;

public class ATMain {

    public static void main(String[] args){

        CassetteImpl cassetteRub = new CassetteImpl(new BanknoteImpl(Currency.RUB, Nominal.FIFTY));
        cassetteRub.addBanknotes(30);

        CassetteImpl cassetteRubSecond = new CassetteImpl(new BanknoteImpl(Currency.RUB, Nominal.HUNDRED));
        cassetteRubSecond.addBanknotes(6);

        CassetteImpl cassetteUsd = new CassetteImpl(new BanknoteImpl(Currency.USD, Nominal.FIFTY));
        cassetteUsd.addBanknotes(16);

        ATMImpl atm = new ATMImpl();
        atm.setCashOutBehavior(new BanknoteBehavior());
        atm.loadCassette(cassetteRub);
        atm.loadCassette(cassetteRubSecond);
        atm.loadCassette(cassetteUsd);

        System.out.println(atm.getBalance());
        atm.cashIn(new BanknoteImpl(Currency.RUB, Nominal.FIVE_HUNDRED), 3);
        System.out.println(atm.getBalance());
        atm.cashOut(Currency.RUB, 1950);
        System.out.println(atm.getBalance());
        atm.cashOut(Currency.USD, 650);
        System.out.println(atm.getBalance());
    }
}