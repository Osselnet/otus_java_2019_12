package ru.otus;

import ru.otus.atm.*;
import ru.otus.atm.commands.*;
import ru.otus.atm.observer.*;
import ru.otus.banknotes.Currency;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DepartmentOfATM {
    private Set<ATM> atms;
    private Command offCmd;
    private Command onCmd;
    private ObserverATM observerATM;

    public DepartmentOfATM(){
        atms = new HashSet<>();
        offCmd = new OffAllAtmCommand(atms);
        onCmd = new OnAllAtmCommand(atms);
        observerATM = new ObserverATMImpl();
    }

    public void addATM(ATM atm){
        atms.add(atm);
        observerATM.addListener((Listener) atm);
    }

    public String getBalanceSummary(){
        String message = "Общий баланс банкоматов\n";
        Map<Currency, Integer> mapBalances = new HashMap<>();
        for(ATM atm : atms){
            Map<Currency, Integer> atmBalance = atm.getBalance();
            atmBalance.forEach((key, value) -> mapBalances.put(
                    key,
                    mapBalances.getOrDefault(key, 0) + value));
        }
        for(Map.Entry<Currency, Integer> element: mapBalances.entrySet()){
            message = message + "Валюта: " + element.getKey().getName() + " сумма: " + element.getValue() + "\n";
        }
        return message;
    }

    public void offAllAtm(){
        offCmd.execute();
    }

    public void onAllAtm(){
        onCmd.execute();
    }

    public void returnToStartState() {
        observerATM.notify(Directives.RESET);
    }
}
