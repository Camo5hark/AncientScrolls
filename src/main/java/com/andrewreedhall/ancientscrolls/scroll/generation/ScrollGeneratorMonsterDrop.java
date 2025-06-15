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

package com.andrewreedhall.ancientscrolls.scroll.generation;

import com.andrewreedhall.ancientscrolls.scroll.Scroll;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.Random;

/**
 * Note: Scroll drop rate is meant to be unaffected by Looting for balancing reasons
 */
public class ScrollGeneratorMonsterDrop extends ScrollGenerator {
    private final EntityType monsterType;

    public ScrollGeneratorMonsterDrop(double probability, EntityType monsterType) {
        super(probability);
        this.monsterType = monsterType;
    }

    @Override
    public void generate(Event event, Scroll scroll) {
        if (!(event instanceof EntityDeathEvent entityDeathEvent) || !entityDeathEvent.getEntityType().equals(this.monsterType) || new Random().nextDouble() > this.probability) {
            return;
        }
        entityDeathEvent.getDrops().add(scroll.createItemStack());
    }
}
