package ru.otus.banknotes;

public enum Nominal {
    FIFTY(50),
    HUNDRED(100),
    FIVE_HUNDRED(500),
    THOUSAND(1000),
    FIVE_THOUSAND(5000);

    private int nominal;

    Nominal(int nominal){
        this.nominal = nominal;
    }

    public int getValue(){
        return nominal;
    }
}