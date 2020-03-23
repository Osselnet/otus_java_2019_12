package ru.otus.banknotes;

public class SimpleEqBanknotesImpl implements EqualsBanknotes {
    @Override
    public boolean equal(Banknote banknoteOne, Banknote banknoteTwo) {
        if(banknoteOne == banknoteTwo) return true;
        if(banknoteOne == null || banknoteTwo == null) return false;
        return banknoteOne.getNominal() == banknoteTwo.getNominal()
                && banknoteOne.getCurrency() == banknoteTwo.getCurrency();
    }
}