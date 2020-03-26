package ru.otus;

import ru.otus.atm.*;
import ru.otus.atm.strategy.BanknoteBehavior;
import ru.otus.banknotes.*;
import ru.otus.banknotes.adapter.*;

public class ATMain {
    public static void main(String[] args) {
        checkObserverWithMemento();
    }

    public static void checkAdapter() {
        BundleOfBanknotes bundle = new BundleOfBanknotes();
        System.out.println(bundle);
        bundle.addBanknotes(new BanknoteImpl(Currency.USD, Nominal.FIFTY), 11);
        System.out.println(bundle);
        bundle.addBanknotes(new BanknoteImpl(Currency.USD, Nominal.FIFTY), 15);
        System.out.println(bundle);
        bundle.addBanknotes(new TicketEuroAdapter(new TicketEuro(100)), 5);
        System.out.println(bundle);
    }

    public static void checkCommand() {
        ATM firstAtm = new ATMImpl();
        ATM secondAtm = new ATMImpl();

        DepartmentOfATM department = new DepartmentOfATM();
        department.addATM(firstAtm);
        department.addATM(secondAtm);

        firstAtm.loadCassette(new CassetteImpl(new BanknoteImpl(Currency.USD, Nominal.HUNDRED)));
        secondAtm.loadCassette(new CassetteImpl(new BanknoteImpl(Currency.USD, Nominal.HUNDRED)));

        department.onAllAtm();
        secondAtm.loadCassette(new CassetteImpl(new BanknoteImpl(Currency.USD, Nominal.HUNDRED)));

        department.offAllAtm();
        secondAtm.loadCassette(new CassetteImpl(new BanknoteImpl(Currency.USD, Nominal.HUNDRED)));
    }

    public static void checkObserverWithMemento() {
        DepartmentOfATM department = new DepartmentOfATM();
        ATM atm1 = getDefaultATM();
        ATM atm2 = getDefaultATM();
        ATM atm3 = getDefaultATM();
        department.addATM(atm1);
        department.addATM(atm2);
        department.addATM(atm3);

        System.out.println("Баланс рублей в atm1 до операций = " + atm1.getBalance(Currency.RUB));
        atm1.cashIn(new BanknoteImpl(Currency.RUB, Nominal.FIFTY), 20);
        System.out.println("Баланс рублей в atm1 после взноса = " + atm1.getBalance(Currency.RUB));
        atm1.cashOut(new BanknoteImpl(Currency.RUB, Nominal.FIFTY), 600);
        System.out.println("Баланс рублей в atm1 после выдачи = " + atm1.getBalance(Currency.RUB));
        System.out.println(department.getBalanceSummary());
    }

    private static ATM getDefaultATM() {

        CassetteImpl cassetteRub = new CassetteImpl(new BanknoteImpl(Currency.RUB, Nominal.FIFTY));
        cassetteRub.addBanknotes(30);

        CassetteImpl cassetteRubSecond = new CassetteImpl(new BanknoteImpl(Currency.RUB, Nominal.HUNDRED));

        CassetteImpl cassetteEmpire = new CassetteImpl(new BanknoteImpl(Currency.EUR, Nominal.HUNDRED));
        cassetteEmpire.addBanknotes(6);

        CassetteImpl cassetteUsd = new CassetteImpl(new BanknoteImpl(Currency.USD, Nominal.FIFTY));
        cassetteUsd.addBanknotes(16);

        ATM atm = new ATMImpl();
        atm.on();
        atm.setCashOutBehavior(new BanknoteBehavior());
        atm.loadCassette(cassetteRub);
        atm.loadCassette(cassetteRubSecond);
        atm.loadCassette(cassetteEmpire);
        atm.loadCassette(cassetteUsd);

        return atm;
    }
}