package com.andrewreedhall.ancientscrolls;

import java.util.Random;

/**
 * Mixin that generates type-based pseudorandom seed entropy for implementing types<br>
 * Depends on <code>Object#hashCode()</code> being unique between implementing types
 */
public interface Entropic {
    /**
     *
     * @return a pseudorandom 48-bit integer that is unique to this type
     */
    default long generateEntropy() {
        return new Random(this.hashCode()).nextLong(0x1000000000000L);
    }

    default Random generateRandom(final long entropy, final long seed, final long... data) {
        long result = 0L;
        int i;
        for (i = 0; i < data.length; ++i) {
            result ^= Long.rotateLeft(mix(data[i]), i * 17);
        }
        result ^= Long.rotateLeft(mix(seed), i * 17);
        ++i;
        result ^= Long.rotateLeft(mix(entropy), i * 17);
        return new Random(result);
    }

    private static long mix(final long data) {
        long result = data;
        result = (result ^ (result >>> 33)) * 0xFF51AFD7ED558CCDL;
        result = (result ^ (result >>> 33)) * 0xC4CEB9fE1A85EC53L;
        result = result ^ (result >>> 33);
        return result;
    }
}
