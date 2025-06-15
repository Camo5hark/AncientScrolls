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

package com.andrewreedhall.ancientscrolls.scroll;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class ScrollManager {
    /**
     * Maps scroll ID hash to scroll<br>
     * Since scroll items store ID as hash in custom model data, this makes it easier to identify scroll item
     */
    static final Map<String, Scroll> REGISTERED_SCROLLS = new HashMap<>();

    public static void registerScroll(JavaPlugin plugin, Scroll scroll) {
        String scrollIDString = scroll.id.toString();
        if (REGISTERED_SCROLLS.containsKey(scrollIDString)) {
            plugin.getLogger().warning("Attempted to register already-registered scroll \"" + scroll.id + "\"");
            return;
        }
        REGISTERED_SCROLLS.put(scrollIDString, scroll);
        // register scroll events if specified in flags
        if (scroll.isFlagHigh(Scroll.FLAG_REGISTER_EVENTS)) {
            plugin().getServer().getPluginManager().registerEvents(scroll, plugin());
        }
    }

    public static void registerScroll(JavaPlugin plugin, Class<? extends Scroll> scrollType) {
        try {
            Constructor<? extends Scroll> scrollConstructor = scrollType.getConstructor();
            try {
                registerScroll(plugin, scrollConstructor.newInstance());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                plugin.getLogger().severe("Scroll could not be registered because constructor failed to create new instance \"" + scrollConstructor.getName() + "\"");
            }
        } catch (NoSuchMethodException e) {
            plugin.getLogger().severe("Scroll could not be registered because zero-parameter constructor was not found in class: \"" + scrollType.getName() + "\"");
        }
    }

    public static Scroll getRegisteredScroll(String idString) {
        return idString == null ? null : REGISTERED_SCROLLS.get(idString);
    }

    public static Scroll getRegisteredScroll(NamespacedKey id) {
        return id == null ? null : getRegisteredScroll(id.toString());
    }

    public static Scroll getRegisteredScroll(ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null || !itemMeta.hasCustomModelData()) {
            return null;
        }
        List<String> itemDataStrings = itemMeta.getCustomModelDataComponent().getStrings();
        for (String itemDataString : itemDataStrings) {
            Scroll registeredScroll = getRegisteredScroll(itemDataString);
            if (registeredScroll != null) {
                return registeredScroll;
            }
        }
        return null;
    }

    public static Collection<Scroll> getRegisteredScrolls() {
        return REGISTERED_SCROLLS.values();
    }
}
