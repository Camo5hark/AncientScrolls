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
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public abstract class AncientScrollsNPC extends AncientScrollsRegistry.Value {
    private static final String PMK_ANCIENT_SCROLLS_NPC = "ancient_scrolls_npc";

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
        getPlayer(npcInstance).setMetadata(PMK_ANCIENT_SCROLLS_NPC, new FixedMetadataValue(plugin(), this));
        plugin().getNPCInstanceHandler().activeNPCInstances.add(npcInstance);
        return npcInstance;
    }

    protected boolean is(final Entity entity) {
        if (!(entity instanceof Player player) || !NPCInstance.is(((CraftPlayer) player).getHandle())) {
            return false;
        }
        final List<MetadataValue> playerAncientScrollsNPCData = player.getMetadata(PMK_ANCIENT_SCROLLS_NPC);
        return !playerAncientScrollsNPCData.isEmpty() && playerAncientScrollsNPCData.getFirst().value() == this;
    }

    public static Player getPlayer(final NPCInstance npcInstance) {
        return (Player) plugin().getServer().getEntity(npcInstance.player.getUUID());
    }
}
