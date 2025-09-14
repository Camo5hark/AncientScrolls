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

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

/**
 * CachedConfig for <code>plugin().getConfig()</code>
 */
public final class CachedConfigDefault extends CachedConfig {
    @Meta(path = "entity-damage-cache-capacity", defaultInt = 1000)
    public int entityDamageCacheCapacity;
    @Meta(path = "item.scroll.max-equipped-scrolls", defaultInt = 9, fixed = true)
    public int item_scroll_maxEquippedScrolls;

    public CachedConfigDefault() {
        super(plugin().getConfig());
    }

    @Override
    protected void saveDefaultConfig() {
        plugin().saveDefaultConfig();
    }

    @Override
    protected void reloadConfig() {
        plugin().reloadConfig();
    }
}
