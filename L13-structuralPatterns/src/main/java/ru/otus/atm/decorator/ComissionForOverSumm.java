package ru.otus.atm.decorator;

public class ComissionForOverSumm extends ComissionChanger {
    public ComissionForOverSumm(ChangerSummOperation changer) {
        super(changer);
    }

    @Override
    public int getFinalSumm() {
        System.out.println("Расчёт суммы с комиссией за превышение порога выдачи");
        return changer.getFinalSumm() > 500 ? changer.getFinalSumm() - 100 : changer.getFinalSumm();
    }
}
