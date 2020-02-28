package ru.otus;

import ru.otus.atm.*;
import ru.otus.banknotes.*;

public class ATMain {

    public static void main(String[] args){

        Cassette cassetteRub = new Cassette(new BanknoteImpl(Currency.RUB, Nominal.FIFTY));
        cassetteRub.addBanknotes(30);

        Cassette cassetteRubSecond = new Cassette(new BanknoteImpl(Currency.RUB, Nominal.HUNDRED));
        cassetteRubSecond.addBanknotes(6);

        Cassette cassetteUsd = new Cassette(new BanknoteImpl(Currency.USD, Nominal.FIFTY));
        cassetteUsd.addBanknotes(16);

        ATM atm = new ATM();
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