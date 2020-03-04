package ru.otus.atm.commands;

import ru.otus.atm.AbstractATM;

import java.util.Collection;

public class OnAllAtmCommand implements Command {
    private final Collection<AbstractATM> atms;

    public OnAllAtmCommand(Collection<AbstractATM> atms){
        this.atms = atms;
    }

    @Override
    public void execute() {
        for(AbstractATM atm : atms){
            atm.on();
        }
        System.out.println("Все банкоматы включены");
    }
}
