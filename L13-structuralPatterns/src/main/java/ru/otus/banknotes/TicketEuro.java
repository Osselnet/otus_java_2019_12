package ru.otus.banknotes;

public class TicketEuro {
    private int nominal;

    public TicketEuro(int nominal){
        this.nominal = nominal;
    }

    public int getNominal(){
        return nominal;
    }
}
