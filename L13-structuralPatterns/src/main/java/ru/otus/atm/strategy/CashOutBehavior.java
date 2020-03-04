package ru.otus.atm.strategy;

import ru.otus.atm.Cassette;
import ru.otus.banknotes.*;

import java.util.List;

public interface CashOutBehavior {
    public BundleOfBanknotes getBundleToCashOut(Currency currency, int summ, List<Cassette> cassettes);
}