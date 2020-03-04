package ru.otus.banknotes.adapter;

import ru.otus.banknotes.*;

public class TicketEuroAdapter implements Banknote {
    private TicketEuro ticket;
    private Nominal nominal;

    public TicketEuroAdapter(TicketEuro ticket){
        this.ticket = ticket;
        for(Nominal nom: Nominal.values()){
            if(nom.getValue() == ticket.getNominal()){
                nominal = nom;
                break;
            }
        }
        if(nominal == null){
            throw new IllegalArgumentException("Номинал евро не конвертируется");
        }
    }

    @Override
    public Nominal getNominal() {
        return nominal;
    }

    @Override
    public Currency getCurrency() {
        return Currency.EUR;
    }

    @Override
    public boolean equals(Object o){
        if(o == null || getClass() != o.getClass()) return false;
        return new SimpleEqBanknotesImpl().equal(this, (Banknote) o);
    }

    @Override
    public int hashCode(){
        return this.getNominal().getValue() + this.getCurrency().hashCode();
    }
}
