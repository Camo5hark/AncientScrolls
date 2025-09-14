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

import org.bukkit.configuration.file.FileConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AccessFlag;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Stream;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

/**
 * An alternative way of accessing Bukkit FileConfiguration fields<br>
 * YAML config fields are discretely cached in memory as fields of this class instead of being stored and accessed through a Map as FileConfiguration does it
 */
public abstract class CachedConfig {
    /**
     * Config field metadata<br>
     * Specifies the YAML path and default value of the field
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    protected @interface Meta {
        String path();
        boolean defaultBoolean() default false;
        int defaultInt() default 0;
        long defaultLong() default 0L;
        float defaultFloat() default 0.0F;
        double defaultDouble() default 0.0;
        String defaultString() default "";
        boolean isStringList() default false;

        /**
         *
         * @return true if the field should not be re-cached when the config is reloaded
         */
        boolean fixed() default false;
    }

    /**
     * The FileConfiguration to cache the fields of
     */
    public final FileConfiguration config;

    /**
     *
     * @param config the FileConfiguration to cache the fields of
     */
    public CachedConfig(final FileConfiguration config) {
        this.config = config;
    }

    /**
     * Saves the default FileConfiguration file if it does not exist yet
     */
    protected abstract void saveDefaultConfig();

    /**
     * Reloads the FileConfiguration of this CachedConfig
     */
    protected abstract void reloadConfig();

    /**
     * Reloads the FileConfiguration and caches all its values to each corresponding @Meta field of this CachedConfig
     * @param reload true if this is a reload, false if this is the initial load
     */
    public void load(final boolean reload) {
        this.saveDefaultConfig();
        this.reloadConfig();
        Stream.of(this.getClass().getFields()).parallel().filter((final Field field) -> {
            if (!field.isAnnotationPresent(Meta.class)) {
                return false;
            }
            final Set<AccessFlag> accessFlags = field.accessFlags();
            return accessFlags.contains(AccessFlag.PUBLIC) && !accessFlags.contains(AccessFlag.STATIC);
        }).forEach((final Field field) -> {
            final Meta meta = field.getAnnotation(Meta.class);
            if (reload && meta.fixed()) {
                return;
            }
            Object defaultValue;
            if (meta.isStringList()) {
                defaultValue = new ArrayList<String>();
            } else {
                switch (field.getType().getSimpleName()) {
                    case "boolean":
                        defaultValue = meta.defaultBoolean();
                        break;
                    case "int":
                        defaultValue = meta.defaultInt();
                        break;
                    case "long":
                        defaultValue = meta.defaultLong();
                        break;
                    case "float":
                        defaultValue = meta.defaultFloat();
                        break;
                    case "double":
                        defaultValue = meta.defaultDouble();
                        break;
                    case "String":
                        defaultValue = meta.defaultString();
                        break;
                    default:
                        return;
                }
            }
            final Object value = this.config.get(meta.path(), defaultValue);
            try {
                field.set(this, value);
            } catch (IllegalAccessException e) {
                plugin().getLogger().severe("Could not write to CachedConfig field: " + field.getName() + ", config file: " + this.config.getName());
                throw new RuntimeException(e);
            }
        });
    }
}
