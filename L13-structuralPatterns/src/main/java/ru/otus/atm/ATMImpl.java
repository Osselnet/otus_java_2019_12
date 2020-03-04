package ru.otus.atm;

import ru.otus.atm.strategy.BanknoteBehavior;
import ru.otus.atm.decorator.ComissionForNotRub;
import ru.otus.atm.decorator.ComissionForOverSumm;
import ru.otus.atm.decorator.DefaultChanger;
import ru.otus.banknotes.*;

public class ATMImpl extends AbstractATM {
    public ATMImpl(){
        cashOutBehavior = new BanknoteBehavior();
    }

    @Override
    public void cashOut(Banknote banknote, int summ){
        if(!isWorkAtm())return;
        System.out.println("Запросили сумму " + summ);
        int finalSumm;
        if(banknote.getCurrency().equals(Currency.RUB)) {
            finalSumm = new ComissionForOverSumm(new DefaultChanger(summ)).getFinalSumm();
        } else {
            finalSumm = new ComissionForNotRub(new DefaultChanger(summ)).getFinalSumm();
        }
        if(finalSumm > getCountBanknotes(banknote) * banknote.getNominal().getValue()){
            System.out.println("Запрошенная сумма превышает наличие денег в банкомате");
        } else {
            BundleOfBanknotes bundleToCashOut = cashOutBehavior.getBundleToCashOut(banknote.getCurrency(), finalSumm, cassettes);
            if(bundleToCashOut.getBanknotes().isEmpty()){
                System.out.println("Невозможно выдать деньги текущим набором банкнот");
            } else {
                saveState();
                System.out.println("К выдаче");
                bundleToCashOut.getBanknotes().forEach((k, v) -> {
                    System.out.println("Номинал:" + k.getNominal() + ", количество: " + v);
                });
                recalculateCassettes(bundleToCashOut);
                System.out.println("Выдана сумма " + finalSumm);
            }
        }
    }
}
