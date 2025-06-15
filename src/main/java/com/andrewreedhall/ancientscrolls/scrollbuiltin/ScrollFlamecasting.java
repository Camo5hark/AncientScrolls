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
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorMonsterDrop;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.inventory.ItemStack;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollFlamecasting extends Scroll {
    public ScrollFlamecasting() {
        super(
                NamespacedKey.fromString("flamecasting", plugin()),
                Scroll.FLAG_REGISTER_EVENTS,
                "Flamecasting",

                "Grants ability to cast fire charges"
        );
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorTreasure(0.3, Chest.class, Structure.BASTION_REMNANT),
                new ScrollGeneratorTreasure(0.1, Chest.class, Structure.FORTRESS),
                new ScrollGeneratorMonsterDrop(0.005, EntityType.BLAZE)
        };
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {}

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem();
        if (itemStack == null) {
            return;
        }
        Block clickedBlock = event.getClickedBlock();
        // clicked block must be air
        if (clickedBlock != null) {
            return;
        }
        Player player = event.getPlayer();
        if (player.getCooldown(Material.FIRE_CHARGE) > 0 || !this.doesPlayerHaveScroll(player) || !itemStack.getType().equals(Material.FIRE_CHARGE)) {
            return;
        }
        itemStack.setAmount(itemStack.getAmount() - 1);
        player.setCooldown(Material.FIRE_CHARGE, 10);
        player.launchProjectile(SmallFireball.class, player.getLocation().getDirection());
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.5F, 1.0F);
    }
}
