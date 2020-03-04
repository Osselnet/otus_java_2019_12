package ru.otus.atm;

import ru.otus.banknotes.*;

import java.util.List;

public interface CashOutBehavior {
    public BundleOfBanknotes getBundleToCashOut(Currency currency, int summ, List<CassetteImpl> cassettes);
}