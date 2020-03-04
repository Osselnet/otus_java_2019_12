package ru.otus.atm;

import ru.otus.banknotes.Banknote;

public interface Cassette {

    public int getMaxCountBanknotes();

    public int getCountBanknotes();

    public void addBanknotes(int countAddBanknotes);

    public int addBanknotesToMax(int countAddBanknotes);

    public void removeBanknotes(int countRemoveBanknotes);

    public int removeBanknotesToMin(int countRemoveBanknotes);

    public int getBalance();

    public Banknote getUsedBanknote();
}
