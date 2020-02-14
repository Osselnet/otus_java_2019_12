package ru.otus;

public class Demo implements MyinterfacClass  {
    @log
    @Override
    public void calcul1(int number) {
        System.out.println(number);
    }
    @log
    @Override
    public void calcul2(int n) {
        System.out.println(n);
    }

    @Override
    public void zaraza(int s) {
        System.out.println("Your number: " + s);
    }
}