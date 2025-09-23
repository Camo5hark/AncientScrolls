package com.andrewreedhall.ancientscrolls.util;

import java.util.Comparator;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class Randomizer<T> implements Comparator<T> {
    public Randomizer() {}

    @Override
    public int compare(final T obj1, final T obj2) {
        return plugin().getUniversalRandom().nextInt(-1, 2);
    }
}
