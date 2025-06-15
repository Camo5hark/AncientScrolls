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

package com.andrewreedhall.ancientscrolls.scrollbuiltin;

import com.andrewreedhall.ancientscrolls.scroll.Scroll;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGenerator;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorTreasure;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.generator.structure.Structure;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollArchaeology extends Scroll {
    private static final int RADIUS = 30;

    public ScrollArchaeology() {
        super(
                NamespacedKey.fromString("archaeology", plugin()),
                Scroll.FLAG_SCHEDULE_PER_AFFECTED_PLAYER,
                "Archaeology",

                "Notifies user of chests, barrels, and spawners within 30 blocks"
        );
        this.scheduledTickPeriod = 200;
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorTreasure(0.2, Chest.class, Structure.TRIAL_CHAMBERS)
        };
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {
        World world = player.getWorld();
        Location location = player.getLocation();
        int blockX = location.getBlockX();
        int blockY = location.getBlockY();
        int blockZ = location.getBlockZ();
        for (int y = -RADIUS; y <= RADIUS; ++y) {
            int by = blockY + y;
            for (int z = -RADIUS; z <= RADIUS; ++z) {
                int bz = blockZ + z;
                for (int x = -RADIUS; x <= RADIUS; ++x) {
                    Block block = world.getBlockAt(blockX + x, by, bz);
                    if (!block.getChunk().isLoaded()) {
                        continue;
                    }
                    String blockName;
                    switch (block.getType()) {
                        default:
                            continue;
                        case Material.CHEST:
                            blockName = "Chest";
                            break;
                        case Material.BARREL:
                            blockName = "Barrel";
                            break;
                        case Material.SPAWNER:
                            blockName = "Spawner";
                            break;
                    }
                    int distance = (int) Math.round(location.distance(block.getLocation()));
                    player.spigot().sendMessage(
                            ChatMessageType.ACTION_BAR,
                            new TextComponent(ChatColor.GOLD + blockName + ChatColor.GREEN + " detected " + distance + " blocks away")
                    );
                    return;
                }
            }
        }
    }

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {}
}
