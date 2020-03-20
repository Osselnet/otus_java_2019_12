package ru.otus.atm.commands;

import ru.otus.atm.ATM;

import java.util.Collection;

public class OnAllAtmCommand implements Command {
    private final Collection<ATM> atms;

    public OnAllAtmCommand(Collection<ATM> atms){
        this.atms = atms;
    }

    @Override
    public void execute() {
        for(ATM atm : atms){
            atm.on();
        }
        System.out.println("Все банкоматы включены");
    }
}
