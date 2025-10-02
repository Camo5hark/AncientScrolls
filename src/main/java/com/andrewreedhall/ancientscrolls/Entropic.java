package com.andrewreedhall.ancientscrolls;

import java.util.Random;

public interface Entropic {
    default long generateEntropy() {
        return new Random(this.hashCode()).nextLong(0x1000000000000L);
    }
}
