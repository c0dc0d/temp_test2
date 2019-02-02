package com.upgrade.campsite.mycamp;

import org.junit.Test;

import java.util.Arrays;

public class ReservationRepositoryTests {

    @Test
    public void testSave() throws InterruptedException {
        Arrays.asList("a1", "a2", "b1", "c2", "c1")
                .parallelStream()
                .forEach(s -> System.out.format("forEach: %s [%s]\n",
                        s, Thread.currentThread().getName()));
    }

}
