package ru.otus.atm.observer;
import java.util.HashSet;
import java.util.Set;

public class ObserverATMImpl implements ObserverATM {
    private Set<Listener> listeners;

    @Override
    public void addListener(Listener listener) {
        if (this.listeners == null){
            listeners = new HashSet<>();
        }
        listeners.add(listener);
    }

    @Override
    public void notify(Directives directive) {
        if(listeners != null){
            listeners.forEach(listener -> listener.executeDirective(directive));
        }
    }
}
