package ru.otus.atm.observer;

public interface ObserverATM {
    public void addListener(Listener listener);
    public void notify(Directives directive);
}
