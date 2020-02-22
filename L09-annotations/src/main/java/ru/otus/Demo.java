package ru.otus;

import ru.otus.framework.TestRunner;

import java.lang.reflect.InvocationTargetException;

public class Demo {
    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        TestRunner.run(SimpleTest.class);
    }
}