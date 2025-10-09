package com.andrewreedhall.ancientscrolls;

import org.bukkit.NamespacedKey;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

/**
 * Base class for registry values with a {@link NamespacedKey}.
 */
public abstract class AncientScrollsResource {
    /**
     * The unique key identifying this value.
     */
    public final NamespacedKey key;

    /**
     * Constructs a new value with the given key.
     *
     * @param key the key
     */
    public AncientScrollsResource(final NamespacedKey key) {
        this.key = key;
    }

    @Override
    public int hashCode() {
        return this.key.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return obj == this || (obj instanceof AncientScrollsResource && obj.hashCode() == this.hashCode());
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "{key=\"" + this.key + "\"}";
    }

    /**
     * Creates a key using the plugin's namespace.
     *
     * @param id the key id
     * @return the namespaced key
     */
    protected static NamespacedKey fromAncientScrollsNamespace(final String id) {
        return NamespacedKey.fromString(id, plugin());
    }
}
