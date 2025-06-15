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
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorMonsterDrop;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollMycology extends Scroll {
    public ScrollMycology() {
        super(
                NamespacedKey.fromString("mycology", plugin()),
                Scroll.FLAG_REGISTER_EVENTS,
                "Mycology",

                "All mushrooms replenish +1 hunger",
                "Red mushrooms grant regeneration",
                "Brown mushrooms grant strength"
        );
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorMonsterDrop(0.01, EntityType.BOGGED),
                new ScrollGeneratorMonsterDrop(0.02, EntityType.WITCH)
        };
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {}

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack itemStack = event.getItem();
        Player player = event.getPlayer();
        if (event.getClickedBlock() != null || itemStack == null || !this.doesPlayerHaveScroll(player)) {
            return;
        }
        Material itemStackType = itemStack.getType();
        PotionEffectType effectType;
        switch (itemStackType) {
            default:
                return;
            case Material.RED_MUSHROOM:
                effectType = PotionEffectType.REGENERATION;
                break;
            case Material.BROWN_MUSHROOM:
                effectType = PotionEffectType.STRENGTH;
                break;
        }
        int foodLevel = player.getFoodLevel();
        if (foodLevel >= 20) {
            player.playSound(player, Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0F, 1.0F);
            return;
        }
        itemStack.setAmount(itemStack.getAmount() - 1);
        player.setCooldown(itemStackType, 100);
        player.setFoodLevel(foodLevel + 1);
        player.addPotionEffect(new PotionEffect(effectType, 60, 1, false, false));
        player.playSound(player, Sound.ENTITY_PLAYER_BURP, 1.0F, 1.0F);
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
    }
}
