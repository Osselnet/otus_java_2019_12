package ru.otus.atm.memento;

import java.io.Serializable;
import java.util.Arrays;

public class Memento implements Serializable {
    private final byte[] state;

    public Memento(byte[] state) {
        this.state = Arrays.copyOf(state, state.length);
    }

    public byte[] getState() {
        return state;
    }
}
