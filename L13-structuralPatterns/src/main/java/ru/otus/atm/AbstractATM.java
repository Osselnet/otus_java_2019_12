package ru.otus.atm;

import ru.otus.atm.memento.Memento;
import ru.otus.atm.strategy.CashOutBehavior;
import ru.otus.atm.observer.*;
import ru.otus.banknotes.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractATM implements Listener {

    protected List<Cassette> cassettes = new ArrayList<>();
    protected CashOutBehavior cashOutBehavior;
    protected int workState = 0;
    private Memento undo = null;

    public Memento getUndo() {
        return undo;
    }

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

    public void off() {
        workState = 0;
    }

    public void on() {
        workState = 1;
    }

    protected void saveState(){
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            undo = new Memento(bos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCassette(Cassette cassette){
        cassettes.add(cassette);
        System.out.println("Кассета установлена");
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
            for(Cassette cassette: cassettes){
                if(cassette.getUsedBanknote().equals(banknote)){
                    countForAdd -= cassette.addBanknotesToMax(countForAdd);
                    if(countForAdd <= 0) break;
                }
            }
        }
    }

    protected boolean isWorkAtm(){
        if(workState == 1){
            return true;
        } else {
            System.out.println("Банкомат не работает");
            return false;
        }
    }

    public abstract void cashOut(Banknote banknote, int summ);

    public void returnToStartState(){
        while (undo != null){
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(undo.getState());
                ObjectInputStream ois = new ObjectInputStream(bis);
                AbstractATM restoreAtm = (AbstractATM) ois.readObject();
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

    protected int getCountBanknotes(Banknote banknote){
        return cassettes.stream()
                .filter(cassette -> cassette.getUsedBanknote().equals(banknote))
                .mapToInt(Cassette::getCountBanknotes)
                .sum();
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
