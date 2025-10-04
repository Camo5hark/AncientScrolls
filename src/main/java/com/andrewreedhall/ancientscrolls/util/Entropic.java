package com.andrewreedhall.ancientscrolls.util;

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
        final byte[] entropyComponents = new byte[Long.BYTES];
        new Random(this.hashCode()).nextBytes(entropyComponents);
        long entropy = 0L;
        for (int i = 0; i < entropyComponents.length; ++i) {
            entropy |= (long) entropyComponents[i] << (i * Byte.SIZE);
        }
        return entropy;
    }

    default Random generateRandom(final long entropy, final long inputSeed, final long... data) {
        // Based on SplitMix64's algorithm
        long outputSeed = 0L;
        int i = 0;
        for (; i < data.length; ++i) {
            outputSeed = mixAndRotate(data[i], outputSeed, i);
        }
        outputSeed = mixAndRotate(inputSeed, outputSeed, i);
        outputSeed = mixAndRotate(entropy, outputSeed, i + 1);
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
