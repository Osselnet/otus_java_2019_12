package ru.otus.banknotes;

import java.util.HashMap;
import java.util.Map;

public class BundleOfBanknotes {

    private final Map<Banknote, Integer> banknotes = new HashMap<>();

    public Map<Banknote, Integer> getBanknotes() {
        return banknotes;
    }

    public void addBanknotes(Banknote banknote, int count) {
        if (count < 0) throw new IllegalArgumentException();
        banknotes.put(banknote, banknotes.getOrDefault(banknote, 0) + count);
    }

    public void removeBanknotes(Banknote banknote, int count) {
        if (banknotes.get(banknote) - count < 0) throw new IllegalArgumentException();
        banknotes.put(banknote, banknotes.getOrDefault(banknote, 0) - count);
    }

    public int getCountBanknotes(Banknote banknote) {
        return banknotes.getOrDefault(banknote, 0);
    }

    public int getCountBanknotes() {
        return banknotes.values().stream().reduce(0, (x, y) -> x + y);
    }

    public String toString() {
        return "====Пачка банкнот====\n" + banknotes.entrySet().stream()
                .map(x -> "Валюта: " + x.getKey().getCurrency().getName()
                        + "\nНоминал: " + x.getKey().getNominal().getValue()
                        + "\nКоличество банкнот: " + x.getValue())
                .reduce("", (x, y) -> x + y + "\n")
                + "=======end=======";
    }
}