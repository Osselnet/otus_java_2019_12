package ru.otus.banknotes;

public class BanknoteImpl implements Banknote {

    private Nominal nominal;
    private Currency currency;

     public BanknoteImpl(Currency currency, Nominal nominal){
        this.nominal = nominal;
        this.currency = currency;
    }

    @Override
    public Nominal getNominal(){
        return nominal;
    }

    @Override
    public Currency getCurrency(){
        return currency;
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