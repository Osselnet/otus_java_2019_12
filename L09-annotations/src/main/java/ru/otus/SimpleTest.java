package ru.otus;

import ru.otus.framework.After;
import ru.otus.framework.Before;
import ru.otus.framework.Test;

import static ru.otus.framework.AssertEquals.assertEquals;

public class SimpleTest {
    private int a;
    private int b;

    @Before
    public void before() {
        a = 100;
        b = 10;
    }

    @After
    public void after() {
        a = 0;
        b = 0;
    }

    @Test(name = "Testing sum")
    public void testSum() {
        int c = a + b;

        assertEquals(110, c);
    }

    @Test
    public void testException() {
        throw new RuntimeException("Something wrong...");
    }

    @Test(name = "Testing wrong division")
    public void testWrongDiv() {
        int c = a / b;

        assertEquals(11, c);
    }
}