package ru.otus.atm.commands;

import ru.otus.atm.ATM;

import java.util.Collection;

public class OffAllAtmCommand implements Command {
    private final Collection<ATM> atms;

    public OffAllAtmCommand(Collection<ATM> atms){
        this.atms = atms;
    }

    @Override
    public void execute() {
        for(ATM atm : atms){
            atm.off();
        }
        System.out.println("Все банкоматы выключены");
    }
}
