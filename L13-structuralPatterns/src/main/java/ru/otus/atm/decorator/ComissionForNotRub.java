package ru.otus.atm.decorator;

public class ComissionForNotRub extends ComissionChanger {
    public ComissionForNotRub(ChangerSummOperation changer) {
        super(changer);
    }

    @Override
    public int getFinalSumm() {
        System.out.println("Расчёт суммы с комиссией за выдачу в иностранной валюте");
        return changer.getFinalSumm() >= 100 ? changer.getFinalSumm()-50 : changer.getFinalSumm();
    }
}
