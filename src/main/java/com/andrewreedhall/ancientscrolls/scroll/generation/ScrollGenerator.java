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
import org.bukkit.event.Event;

import java.util.UUID;

public abstract class ScrollGenerator {
    private final int hash;
    protected final double probability;

    protected ScrollGenerator(double probability) {
        this.hash = UUID.randomUUID().hashCode();
        this.probability = probability;
    }

    public abstract void generate(Event event, Scroll scroll);

    @Override
    public int hashCode() {
        return this.hash;
    }
}
