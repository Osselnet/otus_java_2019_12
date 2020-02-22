package ru.otus.framework;

public class AssertEquals {
    public static void assertEquals(int expected, int actual) {
        if (expected != actual)
            throw new AssertionError(String.format("Expected %d, but has %d!", expected, actual));
    }
}