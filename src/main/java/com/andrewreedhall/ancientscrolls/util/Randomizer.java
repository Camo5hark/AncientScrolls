/*
AncientScrolls SpigotMC plugin
Copyright (C) 2025  Andrew Hall

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.

License file: <project-root>/COPYING
GitHub repo URL: www.github.com/Camo5hark/AncientScrolls
 */

package com.andrewreedhall.ancientscrolls.util;

import com.andrewreedhall.ancientscrolls.item.scroll.ItemScroll;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

/**
 * Comparator that randomizes (shuffles) elements of a list
 * @param <T>
 */
public final class Randomizer<T> implements Comparator<T> {
    /**
     * Global randomizer for scrolls
     */
    public static final Randomizer<ItemScroll> SCROLL_RANDOMIZER = new Randomizer<>();

    private Random random = plugin().getUniversalRandom();

    public Randomizer() {}

    @Override
    public int compare(final T obj1, final T obj2) {
        return random.nextInt(-1, 2);
    }

    /**
     * Sorts a list using this randomizer
     * @param list the list to randomize (shuffle)
     * @param random null for <code>plugin().getUniversalRandom()</code>
     */
    public void sort(final List<T> list, final Random random) {
        this.random = random == null ? plugin().getUniversalRandom() : random;
        list.sort(this);
    }
}
