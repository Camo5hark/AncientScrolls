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
}
