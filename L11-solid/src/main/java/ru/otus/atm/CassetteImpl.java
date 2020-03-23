package ru.otus.atm;

import ru.otus.banknotes.*;

public class CassetteImpl implements Cassette {
    private Banknote banknote;
    private BundleOfBanknotes bundleOfBanknotes = new BundleOfBanknotes();
    private int maxCountBanknotes = 50;

    public CassetteImpl(BanknoteImpl banknoteImpl) {
        this.banknote = banknoteImpl;
    }

    public CassetteImpl(BanknoteImpl banknote, int maxCount) {
        this.banknote = banknote;
        maxCountBanknotes = maxCount;
    }

    @Override
    public int getMaxCountBanknotes() {
        return maxCountBanknotes;
    }

    @Override
    public int getCountBanknotes() {
        return bundleOfBanknotes.getCountBanknotes(banknote);
    }

    @Override
    public void addBanknotes(int countAddBanknotes) {
        if (bundleOfBanknotes.getCountBanknotes() + countAddBanknotes > maxCountBanknotes)
            throw new RuntimeException("Кассета переполнится");
        this.bundleOfBanknotes.addBanknotes(banknote, countAddBanknotes);
    }

    @Override
    public int addBanknotesToMax(int countAddBanknotes) {
        int countFree = maxCountBanknotes - bundleOfBanknotes.getCountBanknotes();
        if (countFree >= countAddBanknotes) {
            addBanknotes(countAddBanknotes);
            return countAddBanknotes;
        } else {
            addBanknotes(countFree);
            return countFree;
        }
    }

    @Override
    public void removeBanknotes(int countRemoveBanknotes) {
        if (bundleOfBanknotes.getCountBanknotes() - countRemoveBanknotes < 0)
            throw new RuntimeException("В кассете не хватает банкнот");
        bundleOfBanknotes.removeBanknotes(banknote, countRemoveBanknotes);
    }

    @Override
    public int removeBanknotesToMin(int countRemoveBanknotes) {
        if (getCountBanknotes() >= countRemoveBanknotes) {
            removeBanknotes(countRemoveBanknotes);
            return countRemoveBanknotes;
        } else {
            int countRemoved = getCountBanknotes();
            removeBanknotes(countRemoved);
            return countRemoved;
        }
    }

    @Override
    public int getBalance() {
        return bundleOfBanknotes.getCountBanknotes(banknote) * banknote.getNominal().getValue();
    }

    @Override
    public Banknote getUsedBanknote() {
        return banknote;
    }
}