package ru.otus.banknotes;

public enum Currency {
    RUB("Рубль"),
    USD("Доллар США");

    private String name;

    Currency(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}