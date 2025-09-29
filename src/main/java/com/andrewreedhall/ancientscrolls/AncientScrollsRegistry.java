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

package com.andrewreedhall.ancientscrolls;

import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

/**
 * Simple registry that maps a NamespacedKey to a specific type of value/resource
 * @param <T> the keyed value/resource to map
 */
public final class AncientScrollsRegistry<T extends AncientScrollsRegistry.Value> {
    /**
     * Represents a keyed resource to be stored and mapped by a registry
     */
    public static abstract class Value {
        /**
         * The (per-registry) unique key of this resource
         */
        public final NamespacedKey key;

        public Value(final NamespacedKey key) {
            this.key = key;
        }

        @Override
        public int hashCode() {
            return this.key.hashCode();
        }

        @Override
        public boolean equals(final Object obj) {
            return obj == this || (obj instanceof Value && obj.hashCode() == this.hashCode());
        }

        /**
         *
         * @param id identifier component of the key
         * @return <code>NamespacedKey.fromString("ancientscrolls:" + id)</code>
         */
        public static NamespacedKey fromAncientScrollsNamespace(final String id) {
            return NamespacedKey.fromString(id, plugin());
        }
    }

    private final Map<NamespacedKey, T> registry = new HashMap<>();

    public AncientScrollsRegistry() {}

    /**
     * Attempts to register a resource to this registry<br>
     * If this registry already contains a resource with <code>value.key</code>, then <code>value</code> is not registered to this registry,
     * a warning is printed to the log, and false is returned<br>
     * If <code>value</code> is a Bukkit listener, then it is registered as an event listener
     * @param value the resource to register
     * @return true if successfully registered, false if already registered
     */
    public boolean register(final T value) {
        if (this.registry.containsKey(value.key)) {
            plugin().getLogger().warning("Attempted to register duplicate value " + value.key);
            return false;
        }
        this.registry.put(value.key, value);
        if (value instanceof Listener) {
            plugin().registerListener((Listener) value);
        }
        return true;
    }

    /**
     * Instantiates a singleton instance of a resource using reflection and attempts to register it to this registry<br>
     * <code>valueType</code> is searched for a zero-argument constructor, which is then instantiated and passed into <code>#register(Value)</code><br>
     * This method has the same restrictions and return values specified by <code>#register(Value)</code>
     * @param valueType the class of the resource to register (must contain a zero-argument constructor)
     * @return true if successfully registered, false if already registered
     * @throws RuntimeException if no zero-argument constructor was found in <code>valueType</code> or if <code>valueType</code> could not be instantiated
     * @see #register(Value)
     */
    public boolean register(final Class<? extends T> valueType) {
        try {
            final Constructor<? extends T> zeroArgConstructor = valueType.getConstructor();
            try {
                return this.register(zeroArgConstructor.newInstance());
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                plugin().getLogger().severe("Could not instantiate " + valueType.getName());
                throw new RuntimeException(e);
            }
        } catch (final NoSuchMethodException e) {
            plugin().getLogger().severe("Could not find zero argument constructor for " + valueType.getName());
            throw new RuntimeException(e);
        }
    }

    /**
     * Attempts to register all the resources in <code>values</code> using <code>#register(Value)</code><br>
     * If a value fails to register, all subsequent values are tried and false is returned
     * @param values all the resources to register
     * @return true if all resources were successfully registered, false if not
     * @see #register(Value)
     */
    @SafeVarargs
    public final boolean registerAll(final T... values) {
        boolean registeredAll = true;
        for (final T value : values) {
            if (!this.register(value)) {
                registeredAll = false;
            }
        }
        return registeredAll;
    }

    /**
     * Attempts to register all the resources in <code>valueTypes</code> using <code>#register(Class)</code><br>
     * If a value fails to register, all subsequent values are tried and false is returned
     * @param valueTypes all the resource types to register
     * @return true if all resources were successfully registered, false if not
     * @see #register(Class)
     */
    @SafeVarargs
    public final boolean registerAll(final Class<? extends T>... valueTypes) {
        boolean registeredAll = true;
        for (final Class<? extends T> valueType : valueTypes) {
            if (!this.register(valueType)) {
                registeredAll = false;
            }
        }
        return registeredAll;
    }

    /**
     *
     * @param key a key
     * @return the value mapped by this key or null if no value is mapped
     */
    public T get(final NamespacedKey key) {
        return this.registry.get(key);
    }

    /**
     *
     * @return a collection of all values mapped by this registry
     */
    public Collection<T> getAll() {
        return this.registry.values();
    }
}
