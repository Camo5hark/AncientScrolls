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
 * Generic registry for values identified by {@link NamespacedKey}.
 * @param <T> the value type extending {@link Value}
 */
public final class AncientScrollsRegistry<T extends AncientScrollsRegistry.Value> {
    /**
     * Base class for registry values with a {@link NamespacedKey}.
     */
    public static abstract class Value {
        /**
         * The unique key identifying this value.
         */
        public final NamespacedKey key;

        /**
         * Constructs a new value with the given key.
         * @param key the key
         */
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

        @Override
        public String toString() {
            return this.getClass().getName() + "{key=\"" + this.key + "\"}";
        }

        /**
         * Creates a key using the plugin's namespace.
         * @param id the key id
         * @return the namespaced key
         */
        protected static NamespacedKey fromAncientScrollsNamespace(final String id) {
            return NamespacedKey.fromString(id, plugin());
        }
    }

    private final Map<NamespacedKey, T> registry = new HashMap<>();

    /**
     * Creates an empty registry.
     */
    public AncientScrollsRegistry() {}

    /**
     * Registers a value instance.
     * @param value the value to register
     * @return true if successful, false if duplicate
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
     * Registers a value using its zero-argument constructor.
     * @param valueType the class to register
     * @return true if successful, false on error or duplicate
     * @throws RuntimeException if instantiation fails
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
     * Registers multiple value instances.
     * @param values the values to register
     * @return true if all were registered, false otherwise
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
     * Registers multiple value classes.
     *
     * @param valueTypes the classes to register
     * @return true if all were registered, false otherwise
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
     * Gets a registered value by key.
     * @param key the key
     * @return the registered value, or null if not found
     */
    public T get(final NamespacedKey key) {
        return this.registry.get(key);
    }

    /**
     * Returns all registered values.
     * @return collection of all registered values
     */
    public Collection<T> getAll() {
        return this.registry.values();
    }
}
