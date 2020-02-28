package ru.otus.atm;

import ru.otus.banknotes.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ATM {
    protected List<Cassette> cassettes = new ArrayList<>();
    protected CashOutBehavior cashOutBehavior;
    protected int workState = 0;

    public int getWorkState() {
        return workState;
    }

    public List<Cassette> getCassetes() {
        return cassettes;
    }

    public CashOutBehavior getCashOutBehavior() {
        return cashOutBehavior;
    }

    public void setCashOutBehavior(CashOutBehavior cashOutBehavior){
        this.cashOutBehavior = cashOutBehavior;
    }

    public int getBalance(Currency currency){
        return cassettes.stream()
                .filter(cassette -> cassette.getUsedBanknote().getCurrency() == currency)
                .mapToInt(Cassette::getBalance).sum();
    }

    public void loadCassette(Cassette cassette){
        cassettes.add(cassette);
        System.out.println("Кассета установлена");
    }

    public void cashIn(Banknote banknote, int count){
        if(
                cassettes.stream()
                        .filter(cassette -> cassette.getUsedBanknote().equals(banknote))
                        .mapToInt(x->x.getMaxCountBanknotes() - x.getCountBanknotes())
                        .sum() < count
        ) {
            System.out.println("Нужные кассеты не установлены или в них не хватит места");
        } else {
            int countForAdd = count;
            for(Cassette cassette: cassettes){
                if(cassette.getUsedBanknote().equals(banknote)){
                    countForAdd -= cassette.addBanknotesToMax(countForAdd);
                    if(countForAdd <= 0) break;
                }
            }
        }
    }

    public void cashOut(Banknote banknote, int summ){
        System.out.println("Запросили сумму " + summ);
        if(summ > getCountBanknotes(banknote) * banknote.getNominal().getValue()){
            System.out.println("Запрошенная сумма превышает наличие денег в банкомате");
        } else {
            BundleOfBanknotes bundleToCashOut = cashOutBehavior.getBundleToCashOut(banknote.getCurrency(), summ, cassettes);
            if(bundleToCashOut.getBanknotes().isEmpty()){
                System.out.println("Невозможно выдать деньги текущим набором банкнот");
            } else {
                System.out.println("К выдаче");
                bundleToCashOut.getBanknotes().forEach((k, v) -> {
                    System.out.println("Номинал:" + k.getNominal() + ", количество: " + v);
                });
                recalculateCassettes(bundleToCashOut);
                System.out.println("Выдана сумма " + summ);
            }
        }
    }

    public void cashOut(Currency currency, int summ){
        if(summ > getBalance().get(currency)){
            System.out.println("Запрошенная сумма превышает наличие денег в банкомате на " + String.valueOf(summ - getBalance().get(currency)));
        } else {
            BundleOfBanknotes bundleToCashOut = cashOutBehavior.getBundleToCashOut(currency, summ, cassettes);
            if(bundleToCashOut.getBanknotes().isEmpty()){
                System.out.println("Невозможно выдать деньги текущим набором банкнот");
            } else {
                System.out.println("К выдаче");
                bundleToCashOut.getBanknotes().forEach((k, v) -> {
                    System.out.println("Номинал:" + k.getNominal() + ", количество: " + v);
                });
                recalculateCassettes(bundleToCashOut);
                System.out.println("Выдана сумма " + summ);
            }
        }
    }

    protected int getCountBanknotes(Banknote banknote){
        return cassettes.stream()
                .filter(cassette -> cassette.getUsedBanknote().equals(banknote))
                .mapToInt(Cassette::getCountBanknotes)
                .sum();
    }

    protected void recalculateCassettes(BundleOfBanknotes cashOutBundle){
        cashOutBundle.getBanknotes().forEach((banknote, count) -> {
            for(Cassette cassette: cassettes){
                if(cassette.getUsedBanknote().equals(banknote) && cassette.getCountBanknotes() > 0){
                    count -= cassette.removeBanknotesToMin(count);
                    if(count <= 0) break;
                }
            }
        });
    }

    public Map<Currency, Integer> getBalance(){
        Map<Currency, Integer> result = new HashMap<>();
        cassettes.forEach(cassette -> {
            if(cassette.getCountBanknotes() > 0){
                result.put(
                        cassette.getUsedBanknote().getCurrency(),
                        result.getOrDefault(cassette.getUsedBanknote().getCurrency(), 0)
                                + cassette.getCountBanknotes() * cassette.getUsedBanknote().getNominal().getValue());
            }
        });
        return result;
    }

}