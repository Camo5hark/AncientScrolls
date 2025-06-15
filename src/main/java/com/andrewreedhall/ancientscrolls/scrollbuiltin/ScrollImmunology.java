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
import org.bukkit.NamespacedKey;
import org.bukkit.block.Barrel;
import org.bukkit.block.Chest;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollImmunology extends Scroll {
    private static final Set<PotionEffectType> DE_BUFF_EFFECTS = Set.of(
            PotionEffectType.SLOWNESS,
            PotionEffectType.MINING_FATIGUE,
            PotionEffectType.NAUSEA,
            PotionEffectType.BLINDNESS,
            PotionEffectType.HUNGER,
            PotionEffectType.WEAKNESS,
            PotionEffectType.POISON,
            PotionEffectType.WITHER,
            PotionEffectType.LEVITATION,
            PotionEffectType.UNLUCK,
            PotionEffectType.BAD_OMEN,
            PotionEffectType.DARKNESS,
            PotionEffectType.INFESTED
    );

    public ScrollImmunology() {
        super(
                NamespacedKey.fromString("immunology", plugin()),
                Scroll.FLAG_REGISTER_EVENTS,
                "Immunology",

                "Minimally reduces de-buff effect duration"
        );
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorTreasure(0.4, Chest.class, Structure.STRONGHOLD),
                new ScrollGeneratorMonsterDrop(0.01, EntityType.BREEZE),
                new ScrollGeneratorTreasure(0.2, Barrel.class, Structure.TRIAL_CHAMBERS)
        };
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {}

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityPotionEffect(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        PotionEffect effect = event.getNewEffect();
        if (effect == null) {
            return;
        }
        int effectDuration = effect.getDuration();
        if (effectDuration == -1) {
            return;
        }
        PotionEffectType effectType = effect.getType();
        if (!DE_BUFF_EFFECTS.contains(effectType)) {
            return;
        }
        if (!this.doesPlayerHaveScroll(player)) {
            return;
        }
        // cannot modify effect duration
        int reducedEffectDuration = effectDuration - (effectDuration / 8);
        // instead, schedule delayed task that removes the effect in reducedEffectDuration ticks from now
        plugin().getServer().getScheduler().scheduleSyncDelayedTask(plugin(), () -> player.removePotionEffect(effectType), reducedEffectDuration);
    }
}
