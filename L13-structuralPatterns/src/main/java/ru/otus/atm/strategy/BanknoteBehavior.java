package ru.otus.atm.strategy;

import ru.otus.atm.CassetteImpl;
import ru.otus.banknotes.*;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BanknoteBehavior implements CashOutBehavior, Serializable {
    @Override
    public BundleOfBanknotes getBundleToCashOut(Currency currency, int summ, List<CassetteImpl> cassettes) {
        cassettes.sort(Comparator.comparing(cassette -> cassette.getUsedBanknote().getNominal().getValue()));
        List<CassetteImpl> mutableCassettes = cassettes.stream()
                .filter(cassette -> cassette.getUsedBanknote().getCurrency() == currency)
                .sorted(Comparator.comparing(cassette -> cassette.getUsedBanknote().getNominal().getValue()))
                .collect(Collectors.toList());
        Collections.reverse(mutableCassettes);
        BundleOfBanknotes resultBundle = new BundleOfBanknotes();
        for(CassetteImpl cassette: mutableCassettes){
            int countInCassette = cassette.getCountBanknotes();
            while (countInCassette > 0 && cassette.getUsedBanknote().getNominal().getValue() <= summ && summ > 0){
                resultBundle.addBanknotes(cassette.getUsedBanknote(), 1);
                summ -= cassette.getUsedBanknote().getNominal().getValue();
                countInCassette--;
            }
        }
        if(summ != 0){
            resultBundle.getBanknotes().clear();
        }
        return resultBundle;
    }
 }