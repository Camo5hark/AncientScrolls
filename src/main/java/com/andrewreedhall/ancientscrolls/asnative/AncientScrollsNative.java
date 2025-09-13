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

License file: <project-root>/COPYING
GitHub repo URL: www.github.com/Camo5hark/AncientScrolls
 */

package com.andrewreedhall.ancientscrolls.asnative;

import com.andrewreedhall.ancientscrolls.item.ItemListener;
import com.andrewreedhall.ancientscrolls.asnative.flask.*;
import com.andrewreedhall.ancientscrolls.asnative.scroll.*;
import com.andrewreedhall.ancientscrolls.item.scroll.ItemScrollListener;
import org.bukkit.scheduler.BukkitScheduler;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class AncientScrollsNative {
    private static void postRegisterAll() {
        plugin().registerListeners(
                new ItemListener(),
                new ItemScrollListener(),
                plugin().getEquippedScrollsInventoryHandler(),
                plugin().getGUIInventoryHandler(),
        );
        plugin().scheduleTask((final BukkitScheduler scheduler) -> scheduler.scheduleSyncRepeatingTask(plugin(), plugin().getEquippedScrollsInventoryHandler(), 0L, 1L));
    }

    public static void registerAll() {
        plugin().getLogger().info("Registering native items");
        plugin().getItemRegistry().registerAll(
                // consumption reducers
                ScrollLandscaping.class,
                ScrollCarpentry.class,
                ScrollMasonry.class,
                ScrollNetherSustainability.class,

                // attribute modifiers
                ScrollCobbling.class,
                ScrollLongevity.class,
                ScrollCrossCountry.class,

                // effect negation
                ScrollPestControl.class, // infested
                ScrollArchaicSkincareRoutine.class, // wither // BOSS: WITHER
                ScrollToxicology.class, // poison
                ScrollAdvancedPhysics.class, // levitation
                ScrollEndurance.class, // mining fatigue // BOSS: ELDER GUARDIAN
                ScrollRetinalResearch.class, // darkness
                ScrollArchaicEyeChart.class, // blindness
                ScrollArchaicWorkoutRoutine.class, // slowness
                ScrollMicrobiology.class, // nausea
                ScrollAutotrophy.class, // hunger

                // effect/night vision
                ScrollAquaticLensing.class,
                ScrollMeteorology.class,
                ScrollCanineStudies.class,
                ScrollSpelunking.class,
                ScrollHillclimbing.class,
                ScrollWhalelore.class, // BOSS: ELDER GUARDIAN

                // bonus
                ScrollHymnal.class,
                ScrollHellsBlessing.class,
                ScrollCounterinsurgency.class,

                // powers
                ScrollAcrobatics.class,
                ScrollBrutality.class,
                ScrollAirBending.class,
                ScrollFlameCasting.class,
                ScrollDeflection.class,
                ScrollDruidsAura.class,
                ScrollMycology.class,
                ScrollNetherMycology.class,
                ScrollBallistics.class,
                ScrollFrostCasting.class,
                ScrollRoseCasting.class, // BOSS: WITHER
                ScrollNursing.class,
                ScrollNocturnal.class,
                ScrollNetherLife.class, // BOSS: WITHER
                ScrollVigilance.class,
                ScrollStealth.class, // BOSS: WARDEN
                ScrollImmunityResearch.class,
                ScrollScrollOfTheDragonFang.class, // BOSS: ENDER DRAGON
                ScrollFirewalking.class,
                ScrollNightLife.class, // BOSS: ENDER DRAGON
                ScrollVolcanology.class,
                ScrollSolace.class,
                ScrollEtherealSolace.class, // BOSS: ENDER DRAGON
                ScrollWardenTaming.class, // BOSS: WARDEN
                ScrollScrollOfPoseidon.class, // BOSS: ELDER GUARDIAN
                ScrollBioluminescence.class,

                // end city
                ScrollSniping.class,
                ScrollQuantumTunnelling.class,
                ScrollDurability.class,
                ScrollAeronautics.class,

                // flasks
                FlaskDragonsBlood.class,
                FlaskLaudanum.class,
                FlaskUnstableConcoction.class,
                FlaskHerbalMixture.class,
                FlaskAmbrosia.class,
                FlaskAquavit.class,
                FlaskFermentedCarrotJuice.class
        );
        postRegisterAll();
    }
}
