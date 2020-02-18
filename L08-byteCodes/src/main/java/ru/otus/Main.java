package ru.otus;

public class Main {
    public static void main(String[] args) {
        MyinterfacClass myinterfacClass = MyAop.creatMyClass();
        myinterfacClass.calcul1(8);
        myinterfacClass.calcul2(4);
        myinterfacClass.calcul2(4, 6);
        myinterfacClass.zaraza(4);
    }
}