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

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;
import static com.andrewreedhall.ancientscrolls.scroll.ScrollManager.registerScroll;

public final class BuiltinScrolls {
    public static void register() {
        plugin().getLogger().info("Registering built-in scrolls");

        // Effect negation
        registerScroll(plugin(), ScrollToxicology.class); // poison
        registerScroll(plugin(), ScrollArchaicWorkoutRoutine.class); // weakness
        registerScroll(plugin(), ScrollArchaicSkincareRoutine.class); // wither
        registerScroll(plugin(), ScrollLumegenesis.class); // darkness
        registerScroll(plugin(), ScrollEyeChart.class); // blindness
        registerScroll(plugin(), ScrollAutotrophy.class); // hunger
        registerScroll(plugin(), ScrollAerobics.class); // slowness
        registerScroll(plugin(), ScrollModulation.class); // levitation

        // Attribute modifiers
        registerScroll(plugin(), ScrollLongevity.class); // extra health
        registerScroll(plugin(), ScrollCobbling.class); // speed

        // Damage reduction
        registerScroll(plugin(), ScrollHymnal.class); // undead
        registerScroll(plugin(), ScrollHellsBlessing.class); // nether

        // Misc
        registerScroll(plugin(), ScrollMetabolism.class);
        registerScroll(plugin(), ScrollImmunology.class);
        registerScroll(plugin(), ScrollAeronautics.class);
        registerScroll(plugin(), ScrollBioluminescence.class);
        registerScroll(plugin(), ScrollMartyrdom.class);
        registerScroll(plugin(), ScrollFlamecasting.class);
        registerScroll(plugin(), ScrollFrostcasting.class);
        registerScroll(plugin(), ScrollBallistics.class);
        registerScroll(plugin(), ScrollSniping.class);
        registerScroll(plugin(), ScrollPestControl.class);
        registerScroll(plugin(), ScrollStealth.class);
        registerScroll(plugin(), ScrollVigilance.class);
        registerScroll(plugin(), ScrollCarpetbombing.class);
        registerScroll(plugin(), ScrollNursing.class);
        registerScroll(plugin(), ScrollNocturn.class);
        registerScroll(plugin(), ScrollAcrobatics.class);
        registerScroll(plugin(), ScrollSolace.class);
        registerScroll(plugin(), ScrollArchaeology.class);
        registerScroll(plugin(), ScrollTunneling.class);
        registerScroll(plugin(), ScrollVolcanology.class);
        registerScroll(plugin(), ScrollCanineStudies.class);
        registerScroll(plugin(), ScrollCrossCountry.class);
        registerScroll(plugin(), ScrollSnowStepping.class);
        registerScroll(plugin(), ScrollDruidsAura.class);
        registerScroll(plugin(), ScrollCrossCountry.class);
        registerScroll(plugin(), ScrollMasonry.class);
        registerScroll(plugin(), ScrollMycology.class);
        registerScroll(plugin(), ScrollAqualensing.class);
        registerScroll(plugin(), ScrollSpelunking.class);
        registerScroll(plugin(), ScrollBrutality.class);
        registerScroll(plugin(), ScrollRaidwatching.class);
        registerScroll(plugin(), ScrollForestry.class);
        registerScroll(plugin(), ScrollWoodworking.class);
        registerScroll(plugin(), ScrollDeflection.class);
    }
}
