package ru.otus;

import ru.otus.atm.*;
import ru.otus.atm.strategy.BanknoteBehavior;
import ru.otus.banknotes.*;
import ru.otus.banknotes.adapter.*;

public class ATMain {
    public static void main(String[] args){
        checkObserverWithMemento();
    }

    public static void checkAdapter(){
        BundleOfBanknotes bundle = new BundleOfBanknotes();
        System.out.println(bundle);
        bundle.addBanknotes(new BanknoteImpl(Currency.USD, Nominal.FIFTY), 11);
        System.out.println(bundle);
        bundle.addBanknotes(new BanknoteImpl(Currency.USD, Nominal.FIFTY), 15);
        System.out.println(bundle);
        bundle.addBanknotes(new TicketEuroAdapter(new TicketEuro(100)), 5);
        System.out.println(bundle);
    }

    public static void checkCommand(){
        AbstractATM firstAtm = new ATMImpl();
        AbstractATM secondAtm = new ATMImpl();

        DepartmentOfATM department = new DepartmentOfATM();
        department.addATM(firstAtm);
        department.addATM(secondAtm);

        firstAtm.loadCassette(new Cassette(new BanknoteImpl(Currency.USD,Nominal.HUNDRED)));
        secondAtm.loadCassette(new Cassette(new BanknoteImpl(Currency.USD,Nominal.HUNDRED)));

        department.onAllAtm();
        secondAtm.loadCassette(new Cassette(new BanknoteImpl(Currency.USD,Nominal.HUNDRED)));

        department.offAllAtm();
        secondAtm.loadCassette(new Cassette(new BanknoteImpl(Currency.USD,Nominal.HUNDRED)));
    }

    public static void checkObserverWithMemento(){
        DepartmentOfATM department = new DepartmentOfATM();
        AbstractATM atm1 = getDefaultATM();
        AbstractATM atm2 = getDefaultATM();
        AbstractATM atm3 = getDefaultATM();
        department.addATM(atm1);
        department.addATM(atm2);
        department.addATM(atm3);

        System.out.println("Баланс рублей в atm1 до операций = " + atm1.getBalance(Currency.RUB));
        atm1.cashIn(new BanknoteImpl(Currency.RUB, Nominal.FIFTY), 20);
        System.out.println("Баланс рублей в atm1 после взноса = " + atm1.getBalance(Currency.RUB));
        atm1.cashOut(new BanknoteImpl(Currency.RUB, Nominal.FIFTY), 600);
        System.out.println("Баланс рублей в atm1 после выдачи = " + atm1.getBalance(Currency.RUB));
        //atm2.cashOut(new BanknoteImpl(Currency.RUB, Nominal.FIFTY), 100);

        //department.returnToStartState();
        //System.out.println("Баланс рублей в atm1 после сброса = " + atm1.getBalance(Currency.RUB));

        System.out.println(department.getBalanceSummary());

    }


    private static ATMImpl getDefaultATM(){

        Cassette cassetteRub = new Cassette(new BanknoteImpl(Currency.RUB, Nominal.FIFTY));
        cassetteRub.addBanknotes(30);

        Cassette cassetteRubSecond = new Cassette(new BanknoteImpl(Currency.RUB, Nominal.HUNDRED));

        Cassette cassetteEmpire = new Cassette(new BanknoteImpl(Currency.EUR, Nominal.HUNDRED));
        cassetteEmpire.addBanknotes(6);

        Cassette cassetteUsd = new Cassette(new BanknoteImpl(Currency.USD, Nominal.FIFTY));
        cassetteUsd.addBanknotes(16);

        ATMImpl atm = new ATMImpl();
        atm.on();
        atm.setCashOutBehavior(new BanknoteBehavior());
        atm.loadCassette(cassetteRub);
        atm.loadCassette(cassetteRubSecond);
        atm.loadCassette(cassetteEmpire);
        atm.loadCassette(cassetteUsd);

        return atm;
    }
}