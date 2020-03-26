package ru.otus.atm.decorator;

public class DefaultChanger implements ChangerSummOperation {
    private int summ;

    public DefaultChanger(int summ){
        this.summ = summ;
    }

    @Override
    public int getFinalSumm() {
        return summ;
    }
}
