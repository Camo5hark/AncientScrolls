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
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

/**
 * Base class for registry values with a {@link NamespacedKey}.
 */
public abstract class AncientScrollsResource implements Configurable {
    /**
     * The unique key identifying this value.
     */
    public final NamespacedKey key;
    private final File configFile;
    private final YamlConfiguration config;

    // START CONFIG
    @Meta(path = "generation.enabled", defaultBoolean = true)
    public boolean generation_enabled;
    @Meta(path = "generation.probability-scalar", defaultDouble = 1.0)
    public double generation_probabilityScalar;
    // END CONFIG

    /**
     * Constructs a new value with the given key.
     * @param key the key
     */
    public AncientScrollsResource(final NamespacedKey key) {
        this.key = key;
        this.configFile = this.getConfigFile();
        this.config = new YamlConfiguration();
        this.addConfigDefaultValues(this.config);
    }

    protected abstract File getConfigFile();

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

    @Override
    public Configuration getConfig() {
        return this.config;
    }

    @Override
    public void saveDefaultConfig() {
        if (this.configFile.exists()) {
            return;
        }
        final File configFileParentDir = this.configFile.getParentFile();
        try {
            if ((!configFileParentDir.exists() && !configFileParentDir.mkdirs()) || !this.configFile.createNewFile()) {
                throw new IOException("Config file creation failed: " + this.configFile.getAbsolutePath());
            }
            this.config.options().copyDefaults(true);
            this.config.save(this.configFile);
            this.config.options().copyDefaults(false);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reloadConfig() {
        try {
            this.config.load(this.configFile);
        } catch (final IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
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
