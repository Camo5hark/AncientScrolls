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

import org.bukkit.configuration.file.FileConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AccessFlag;
import java.lang.reflect.Field;
import java.util.*;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class CachedConfig {
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    private @interface ConfigInfo {
        String path();
        boolean defaultBoolean() default false;
        int defaultInt() default 0;
        long defaultLong() default 0L;
        float defaultFloat() default 0.0F;
        double defaultDouble() default 0.0;
        String defaultString() default "";
        boolean isStringList() default false;
    }

    @ConfigInfo(path = "event-cache-capacity", defaultInt = 1000)
    public int eventCacheCapacity; //= 1000;

    @ConfigInfo(path = "trim-bonus.enabled", defaultBoolean = true)
    public boolean trimBonus_enabled;
    @ConfigInfo(path = "trim-bonus.damage-reduction-percentage", defaultDouble = 10.0)
    public double trimBonus_damageReductionPercentage; //= 10.0;
    @ConfigInfo(path = "trim-bonus.damage-infliction-percentage", defaultDouble = 5.0)
    public double trimBonus_damageInflictionPercentage; //= 5.0;

    @ConfigInfo(path = "scroll.player-storage.cache-capacity", defaultInt = -1)
    public int scroll_playerStorage_cacheCapacity; //= -1;
    @ConfigInfo(path = "scroll.player-storage.max-stored-scrolls", defaultInt = 5)
    public int scroll_playerStorage_maxStoredScrolls; //= 5;

    public CachedConfig() {
        this.reload();
    }

    public void reload() {
        // TODO
        plugin().getLogger().info("Attempting to save default config");
        plugin().saveDefaultConfig();
        plugin().getLogger().info("Reloading cached config");
        FileConfiguration config = plugin().getConfig();
        Field[] fields = this.getClass().getFields();
        for (Field field : fields) {
            Set<AccessFlag> accessFlags = field.accessFlags();
            if (!accessFlags.contains(AccessFlag.PUBLIC) || accessFlags.contains(AccessFlag.STATIC)) {
                continue;
            }
            ConfigInfo configInfo = field.getAnnotation(ConfigInfo.class);
            if (configInfo == null) {
                plugin().getLogger().severe("Skipping loading cached config field \"" + field.getName() + "\" because it is missing @ConfigInfo");
                continue;
            }
            String typeName = field.getType().getSimpleName();
            Object defaultValue;
            if (configInfo.isStringList()) {
                defaultValue = new ArrayList<String>();
            } else {
                switch (typeName) {
                    default:
                        plugin().getLogger().severe("Skipping loading cached config field \"" + field.getName() + "\" because it's type \"" + typeName + "\" is not supported");
                        continue;
                    case "boolean":
                        defaultValue = configInfo.defaultBoolean();
                        break;
                    case "int":
                        defaultValue = configInfo.defaultInt();
                        break;
                    case "long":
                        defaultValue = configInfo.defaultLong();
                        break;
                    case "float":
                        defaultValue = configInfo.defaultFloat();
                        break;
                    case "double":
                        defaultValue = configInfo.defaultDouble();
                        break;
                    case "String":
                        defaultValue = configInfo.defaultString();
                        break;
                }
            }
            Object value = config.get(configInfo.path(), defaultValue);
            try {
                field.set(this, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
