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

package com.andrewreedhall.ancientscrolls.npc;

import com.andrewreedhall.ancientscrolls.AncientScrollsRegistry;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public abstract class AncientScrollsNPC extends AncientScrollsRegistry.Value {
    public final String name;
    public final NPCInstance.Skin skin;

    public AncientScrollsNPC(final NamespacedKey key, final String name, final NPCInstance.Skin skin) {
        super(key);
        this.name = name;
        this.skin = skin;
    }

    public NPCInstance createInstance(final World world, final Location location) {
        final NPCInstance npcInstance = new NPCInstance(
                this,
                ((CraftWorld) world).getHandle(),
                location.getX(),
                location.getY(),
                location.getZ()
        );
        plugin().getNPCInstanceHandler().activeNPCInstances.add(npcInstance);
        return npcInstance;
    }
}
