package ru.otus.atm;

import ru.otus.atm.memento.Memento;
import ru.otus.atm.observer.Directives;
import ru.otus.atm.observer.Listener;
import ru.otus.atm.strategy.BanknoteBehavior;
import ru.otus.atm.decorator.ComissionForNotRub;
import ru.otus.atm.decorator.ComissionForOverSumm;
import ru.otus.atm.decorator.DefaultChanger;
import ru.otus.atm.strategy.CashOutBehavior;
import ru.otus.banknotes.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ATMImpl implements ATM, Listener, Serializable {
    private List<CassetteImpl> cassettes = new ArrayList<>();
    private CashOutBehavior cashOutBehavior;
    private int workState = 0;
    private Memento undo = null;

    public ATMImpl(){
        cashOutBehavior = new BanknoteBehavior();
    }

    public void loadCassette(CassetteImpl cassette){
        cassettes.add(cassette);
        System.out.println("Кассета установлена");
    }

    public void off() {
        workState = 0;
    }

    public void on() {
        workState = 1;
    }

    public Memento getUndo() {
        return undo;
    }

    public int getWorkState() {
        return workState;
    }

    public List<CassetteImpl> getCassetes() {
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
                .mapToInt(CassetteImpl::getBalance).sum();
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

    public void cashIn(Banknote banknote, int count){
        if(!isWorkAtm())return;
        if(
                cassettes.stream()
                        .filter(cassette -> cassette.getUsedBanknote().equals(banknote))
                        .mapToInt(x->x.getMaxCountBanknotes() - x.getCountBanknotes())
                        .sum() < count
        ) {
            System.out.println("Нужные кассеты не установлены или в них не хватит места");
        } else {
            saveState();
            int countForAdd = count;
            for(CassetteImpl cassette: cassettes){
                if(cassette.getUsedBanknote().equals(banknote)){
                    countForAdd -= cassette.addBanknotesToMax(countForAdd);
                    if(countForAdd <= 0) break;
                }
            }
        }
    }

    public void returnToStartState(){
        while (undo != null){
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(undo.getState());
                ObjectInputStream ois = new ObjectInputStream(bis);
                ATM restoreAtm = (ATM) ois.readObject();
                this.undo = restoreAtm.getUndo();
                this.workState = restoreAtm.getWorkState();
                this.cassettes = restoreAtm.getCassetes();
                this.cashOutBehavior = restoreAtm.getCashOutBehavior();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                break;
            }
        }
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

    @Override
    public void executeDirective(Directives directive) {
        if(directive == Directives.ON){
            on();
        } else if(directive == Directives.OFF){
            off();
        } else if(directive == Directives.RESET){
            returnToStartState();
        }
    }

    private boolean isWorkAtm(){
        if(workState == 1){
            return true;
        } else {
            System.out.println("Банкомат не работает");
            return false;
        }
    }

    private void saveState(){
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            undo = new Memento(bos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getCountBanknotes(Banknote banknote){
        return cassettes.stream()
                .filter(cassette -> cassette.getUsedBanknote().equals(banknote))
                .mapToInt(CassetteImpl::getCountBanknotes)
                .sum();
    }

    private void recalculateCassettes(BundleOfBanknotes cashOutBundle){
        cashOutBundle.getBanknotes().forEach((banknote, count) -> {
            for(CassetteImpl cassette: cassettes){
                if(cassette.getUsedBanknote().equals(banknote) && cassette.getCountBanknotes() > 0){
                    count -= cassette.removeBanknotesToMin(count);
                    if(count <= 0) break;
                }
            }
        });
    }

}
