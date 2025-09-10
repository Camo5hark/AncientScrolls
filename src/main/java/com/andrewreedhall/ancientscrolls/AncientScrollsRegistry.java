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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class AncientScrollsRegistry<T extends AncientScrollsRegistry.Value> {
    public static abstract class Value {
        public final NamespacedKey key;

        public Value(final NamespacedKey key) {
            this.key = key;
        }

        public static NamespacedKey fromAncientScrollsNamespace(final String id) {
            return NamespacedKey.fromString(id, plugin());
        }
    }

    private final Map<NamespacedKey, T> registry = new HashMap<>();

    public AncientScrollsRegistry() {}

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

    public T get(final NamespacedKey key) {
        return this.registry.get(key);
    }

    public Collection<T> getAll() {
        return this.registry.values();
    }
}
