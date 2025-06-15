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
import com.andrewreedhall.ancientscrolls.scroll.generation.ScrollGeneratorTreasure;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Barrel;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.Set;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public class ScrollPestControl extends Scroll {
    private static final Collection<Class<? extends Entity>> PEST_TYPES = Set.of(
            Spider.class,
            Silverfish.class,
            Endermite.class
    );

    public ScrollPestControl() {
        super(
                NamespacedKey.fromString("pestcontrol", plugin()),
                Scroll.FLAG_REGISTER_EVENTS,
                "Pest Control",

                "Negates infested effect",
                "Negates damage from spiders, silverfish, and endermites",
                "Does NOT negate damage from cave spiders!"
        );
        this.generators = new ScrollGenerator[] {
                new ScrollGeneratorMonsterDrop(0.05, EntityType.SILVERFISH),
                new ScrollGeneratorMonsterDrop(0.005, EntityType.SPIDER),
                new ScrollGeneratorTreasure(0.2, Barrel.class, Structure.TRIAL_CHAMBERS)
        };
    }

    @Override
    public void onScheduledTickPerAffectedPlayer(Player player) {}

    @Override
    public void onScheduledTickPerUnaffectedPlayer(Player player) {}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityPotionEffect(EntityPotionEffectEvent event) {
        this.negateEffect(event, PotionEffectType.INFESTED);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        this.negateDamageFrom(event, PEST_TYPES);
    }
}
