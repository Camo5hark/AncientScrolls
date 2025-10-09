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

import org.bukkit.configuration.Configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AccessFlag;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Stream;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public interface Configurable {
    /**
     * Annotation for mapping config values to fields.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Meta {
        /**
         * Config path to read the value from.
         */
        String path();
        /**
         * Default boolean value.
         */
        boolean defaultBoolean() default false;
        /**
         * Default int value.
         */
        int defaultInt() default 0;
        /**
         * Default long value.
         */
        long defaultLong() default 0L;
        /**
         * Default float value.
         */
        float defaultFloat() default 0.0F;
        /**
         * Default double value.
         */
        double defaultDouble() default 0.0;
        /**
         * Default string value.
         */
        String defaultString() default "";
        /**
         * Whether the field is a string list.
         */
        boolean isStringList() default false;
        /**
         * If true, the value is not reloaded.
         */
        boolean fixed() default false;
    }

    Configuration getConfig();
    void saveDefaultConfig();
    void reloadConfig();

    default void loadConfig(final boolean reload) {
        this.saveDefaultConfig();
        this.reloadConfig();
        final Configuration config = this.getConfig();
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
            final Object value = config.get(meta.path(), defaultValue);
            try {
                field.set(this, value);
            } catch (IllegalAccessException e) {
                plugin().getLogger().severe("Could not write to CachedConfig field: " + field.getName() + ", config file: " + config.getName());
                throw new RuntimeException(e);
            }
        });
    }
}
