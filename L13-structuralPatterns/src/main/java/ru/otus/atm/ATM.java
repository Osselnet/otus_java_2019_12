package ru.otus.atm;

import ru.otus.atm.memento.Memento;
import ru.otus.atm.strategy.CashOutBehavior;
import ru.otus.banknotes.*;

import java.util.List;
import java.util.Map;

public interface ATM {

    public Memento getUndo();

    public int getWorkState();

    public List<CassetteImpl> getCassetes();

    public CashOutBehavior getCashOutBehavior();

    public void setCashOutBehavior(CashOutBehavior cashOutBehavior);

    public int getBalance(Currency currency);

    public void off();

    public void on();

    public void loadCassette(CassetteImpl cassette);

    public void cashIn(Banknote banknote, int count);

    public void cashOut(Banknote banknote, int summ);

    public void returnToStartState();

    public Map<Currency, Integer> getBalance();

}