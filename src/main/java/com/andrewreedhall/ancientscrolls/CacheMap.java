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

    License file: COPYING
    Email for contact: camo5hark10@gmail.com

 */

package com.andrewreedhall.ancientscrolls;

import java.util.*;
import java.util.function.Supplier;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

/**
 * A linked hash map that drops entries in FIFO order when capacity is reached
 * @param <K> linked hash map key type
 * @param <V> linked hash map value type
 */
public final class CacheMap<K, V> {
    /**
     * Add all created cache maps to this set so
     */
    private static final Set<CacheMap<?, ?>> ALL_CACHE_MAPS = new HashSet<>();
    private static final Supplier<Integer> MAX_ONLINE_PLAYER_COUNT_GETTER = () -> plugin().getServer().getMaxPlayers();

    private final int hash;
    private final Supplier<Integer> capacityGetter;
    private int capacity = 0;
    private SequencedMap<K, V> cache = null;

    public CacheMap(Supplier<Integer> capacityGetter) {
        // one cache map is never indirectly equal to another cache map
        this.hash = UUID.randomUUID().hashCode();
        ALL_CACHE_MAPS.add(this);
        this.capacityGetter = capacityGetter;
        this.reset();
    }

    @Override
    public int hashCode() {
        return this.hash;
    }

    public void reset() {
        if (this.cache != null && !this.cache.isEmpty()) {
            this.cache.clear();
        }
        this.capacity = this.capacityGetter.get() + 1;
        this.cache = new LinkedHashMap<>(this.capacity + 1, 1.0F);
    }

    public V get(K key) {
        return this.cache.get(key);
    }

    public void put(K key, V value) {
        if (this.cache.size() == this.capacity) {
            this.cache.pollFirstEntry();
        }
        this.cache.put(key, value);
    }

    public Set<K> getKeys() {
        return this.cache.keySet();
    }

    public static void resetAll() {
        plugin().getLogger().info("Resetting all caches");
        for (CacheMap<?, ?> cacheMap : ALL_CACHE_MAPS) {
            cacheMap.reset();
        }
    }

    public static <V> CacheMap<UUID, V> createForOnlinePlayers() {
        return new CacheMap<>(MAX_ONLINE_PLAYER_COUNT_GETTER);
    }
}
