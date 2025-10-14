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

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Stream;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

/**
 * Generic registry for resources identified by {@link NamespacedKey}.
 * @param <T> the resource type extending {@link AncientScrollsResource}
 */
public final class AncientScrollsRegistry<T extends AncientScrollsResource> {

    private final Map<Integer, T> registry = new Int2ObjectOpenHashMap<>();

    /**
     * Creates an empty registry.
     */
    public AncientScrollsRegistry() {}

    /**
     * Registers a resource instance.
     * @param resource the resource to register
     * @return true if successful, false if duplicate
     */
    public boolean register(final T resource) {
        Preconditions.checkArgument(!this.registry.containsKey(resource.keyHash), "Cannot register duplicate resource: " + resource);
        this.registry.put(resource.keyHash, resource);
        if (resource instanceof Listener) {
            plugin().registerListener((Listener) resource);
        }
        return true;
    }

    /**
     * Registers a value using its zero-argument constructor.
     * @param resourceType the class to register
     * @return true if successful, false on error or duplicate
     * @throws RuntimeException if instantiation fails
     */
    public boolean register(final Class<? extends T> resourceType) {
        try {
            final Constructor<? extends T> resourceConstructor = resourceType.getConstructor();
            try {
                return this.register(resourceConstructor.newInstance());
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Registers multiple value instances.
     * @param resources the resources to register
     * @return true if all were registered, false otherwise
     */
    @SafeVarargs
    public final boolean registerAll(final T... resources) {
        boolean registeredAll = true;
        for (final T value : resources) {
            if (!this.register(value)) {
                registeredAll = false;
            }
        }
        return registeredAll;
    }

    /**
     * Registers multiple value classes.
     * @param resourceTypes the classes to register
     * @return true if all were registered, false otherwise
     */
    @SafeVarargs
    public final boolean registerAll(final Class<? extends T>... resourceTypes) {
        boolean registeredAll = true;
        for (final Class<? extends T> valueType : resourceTypes) {
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
        return this.registry.get(key.hashCode());
    }

    public Stream<T> getAll(final Predicate<T> filter, final boolean parallel) {
        Stream<T> resources = parallel ? this.registry.values().parallelStream() : this.registry.values().stream();
        if (filter != null) {
            resources = resources.filter(filter);
        }
        return resources;
    }

    public void loadAllConfigs(final boolean reload) {
        for (final AncientScrollsResource resource : this.registry.values()) {
            resource.loadConfig(reload);
        }
    }
}
