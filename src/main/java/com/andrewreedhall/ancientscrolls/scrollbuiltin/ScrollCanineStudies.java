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

import com.andrewreedhall.ancientscrolls.Utils;
import com.andrewreedhall.ancientscrolls.scroll.Scroll;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGenerator;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorMonsterDrop;
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorTreasure;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Chest;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollCanineStudies extends Scroll {
    public ScrollCanineStudies() {
        super(
                NamespacedKey.fromString("caninestudies", plugin()),
                Scroll.FLAG_REGISTER_EVENTS | Scroll.FLAG_SCHEDULE_PER_AFFECTED_PLAYER,
                "Canine Studies",

                "Grants night vision at night",
                "Increases damage to skeletons, wither skeletons, and skeleton horses by 20%"
        );
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorMonsterDrop(0.005, EntityType.SKELETON),
                new ScrollGeneratorTreasure(0.3, Chest.class, Structure.DESERT_PYRAMID),
                new ScrollGeneratorTreasure(0.4, Chest.class, Structure.STRONGHOLD)
        };
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {
        long time = player.getWorld().getTime();
        PotionEffect nightVisionEffect = player.getPotionEffect(PotionEffectType.NIGHT_VISION);
        if (time < 13000L || time >= 23000L || (nightVisionEffect != null && nightVisionEffect.getDuration() > 220)) {
            return;
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 240, 0, false, false));
    }

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        this.addDamageToEntityByScalar(event, 0.2, Utils.ENTITY_TYPES_SKELETON);
    }
}
