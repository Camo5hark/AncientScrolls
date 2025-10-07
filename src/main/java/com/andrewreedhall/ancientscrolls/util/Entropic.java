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

import java.util.Random;

/**
 * Mixin that generates type-based pseudorandom seed entropy for implementing types<br>
 * Depends on <code>Object#hashCode()</code> being unique between implementing types
 */
public interface Entropic {
    /**
     *
     * @return a pseudorandom 64-bit integer that is highly likely to be unique to this type
     */
    default long generateEntropy() {
        return new Random(this.hashCode()).nextLong();
    }

    default Random generateRandom(final long entropy, final long inputSeed, final long... data) {
        // Based on SplitMix64's algorithm
        long outputSeed = 0L;
        int i = 0;
        for (; i < data.length; ++i) {
            outputSeed = mixAndRotate(data[i], outputSeed, i);
        }
        outputSeed = mixAndRotate(inputSeed, outputSeed, i);
        outputSeed = mixAndRotate(entropy == 0L ? this.generateEntropy() : entropy, outputSeed, i + 1);
        return new Random(outputSeed);
    }

    private static long mixAndRotate(final long data, final long seed, final int i) {
        long mixedData = data;
        mixedData = (mixedData ^ (mixedData >>> 33)) * 0xFF51AFD7ED558CCDL;
        mixedData = (mixedData ^ (mixedData >>> 33)) * 0xC4CEB9fE1A85EC53L;
        mixedData = mixedData ^ (mixedData >>> 33);
        return seed ^ Long.rotateLeft(mixedData, i * 17);
    }
}
