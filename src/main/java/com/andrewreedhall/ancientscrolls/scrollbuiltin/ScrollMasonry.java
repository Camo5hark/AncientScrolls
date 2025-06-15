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
import org.bukkit.NamespacedKey;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollMasonry extends Scroll {
    public ScrollMasonry() {
        super(
                NamespacedKey.fromString("masonry", plugin()),
                Scroll.FLAG_REGISTER_EVENTS,
                "Masonry",

                "20% chance stone-related blocks will not be consumed when placed"
        );
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorTreasure(0.4, Chest.class, Structure.STRONGHOLD),
                new ScrollGeneratorTreasure(0.4, Chest.class, Structure.TRIAL_CHAMBERS)
        };
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {}

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!this.doesPlayerHaveScroll(event.getPlayer())) {
            return;
        }
        ItemStack itemStack = event.getItemInHand();
        int itemStackAmount = itemStack.getAmount();
        if (itemStackAmount >= itemStack.getMaxStackSize() || !itemStack.getType().toString().contains("STONE") || new Random().nextDouble() > 0.2) {
            return;
        }
        itemStack.setAmount(itemStackAmount + 1);
    }
}
