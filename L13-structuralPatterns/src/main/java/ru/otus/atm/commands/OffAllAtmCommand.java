package ru.otus.atm.commands;

import ru.otus.atm.AbstractATM;

import java.util.Collection;

public class OffAllAtmCommand implements Command {
    private final Collection<AbstractATM> atms;

    public OffAllAtmCommand(Collection<AbstractATM> atms){
        this.atms = atms;
    }

    @Override
    public void execute() {
        for(AbstractATM atm : atms){
            atm.off();
        }
        System.out.println("Все банкоматы выключены");
    }
}
