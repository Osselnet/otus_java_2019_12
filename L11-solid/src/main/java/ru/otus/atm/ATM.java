package ru.otus.atm;

import ru.otus.banknotes.Banknote;
import ru.otus.banknotes.Currency;

import java.util.Map;

public interface ATM {
    public void setCashOutBehavior(CashOutBehavior cashOutBehavior);

    public int getBalance(Currency currency);

    public void loadCassette(CassetteImpl cassette);

    public void cashIn(Banknote banknote, int count);

    public void cashOut(Banknote banknote, int summ);

    public void cashOut(Currency currency, int summ);

    public Map<Currency, Integer> getBalance();
}
