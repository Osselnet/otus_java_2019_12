package ru.otus;

import ru.otus.DIYarrayList;

import java.util.Collections;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        DIYarrayList<String> dList1 = new DIYarrayList<String>();
        DIYarrayList<String> dList2 = new DIYarrayList<String>(100);

        String[] strings = new String[100];
        for (int i = 0; i < 100; i++) {
            strings[i] = Integer.toString(new Random().nextInt(100));
            dList2.add("");
        }

        Collections.addAll(dList1, strings);
        Collections.copy(dList2, dList1);
        Collections.sort(dList2, null);
    }
}
