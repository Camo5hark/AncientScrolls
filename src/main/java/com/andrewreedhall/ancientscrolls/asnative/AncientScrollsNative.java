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

import com.andrewreedhall.ancientscrolls.asnative.npc.hunter.NPCHunter;
import com.andrewreedhall.ancientscrolls.asnative.npc.prospectre.NPCProspectre;
import com.andrewreedhall.ancientscrolls.asnative.structure.StructureFishermanRaft;
import com.andrewreedhall.ancientscrolls.asnative.structure.StructurePillagerShip;
import com.andrewreedhall.ancientscrolls.item.ItemListener;
import com.andrewreedhall.ancientscrolls.asnative.flask.*;
import com.andrewreedhall.ancientscrolls.asnative.scroll.*;
import com.andrewreedhall.ancientscrolls.item.scroll.ItemScrollListener;
import com.andrewreedhall.ancientscrolls.structure.StructureListener;
import org.bukkit.scheduler.BukkitScheduler;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public final class AncientScrollsNative {
    private static void postRegisterAll() {
        plugin().registerListeners(
                // item
                new ItemListener(),
                new ItemScrollListener(),
                plugin().getEquippedScrollsInventoryHandler(),
                plugin().getGUIInventoryHandler(),

                // npc
                plugin().getNPCHandler(),

                // structure
                new StructureListener()
        );
        plugin().scheduleTask((final BukkitScheduler scheduler) -> scheduler.scheduleSyncRepeatingTask(
                plugin(),
                plugin().getEquippedScrollsInventoryHandler(),
                0L,
                1L
        ));
        plugin().scheduleTask((final BukkitScheduler scheduler) -> scheduler.scheduleSyncRepeatingTask(
                plugin(),
                plugin().getNPCHandler(),
                0L,
                4L
        ));
    }

    private static void registerItems() {
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
                ScrollWhalelore.class,

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
                ScrollFlexibility.class,
                ScrollDexterity.class,
                ScrollResistance.class, // BOSS: WARDEN
                ScrollPhoenixsAura.class,
                ScrollJousting.class,
                ScrollWitchcraft.class,
                ScrollScrollOfTheDead.class, // BOSS: WITHER
                ScrollMarineBiology.class, // BOSS: ELDER GUARDIAN
                ScrollDragonRunes.class, // BOSS: ENDER DRAGON

                // end city
                ScrollSniping.class,
                ScrollQuantumTunnelling.class,
                ScrollDurability.class,
                ScrollAeronautics.class,
                ScrollHypermobility.class,
                ScrollRocketScience.class,

                // flasks
                FlaskDragonsBlood.class,
                FlaskLaudanum.class,
                FlaskUnstableConcoction.class,
                FlaskHerbalMixture.class,
                FlaskAmbrosia.class,
                FlaskAquavit.class,
                FlaskFermentedCarrotJuice.class
        );
    }

    private static void registerNPCs() {
        plugin().getLogger().info("Registering native NPCs");
        plugin().getNPCRegistry().registerAll(
                NPCProspectre.class,
                NPCHunter.class
        );
    }

    private static void registerStructures() {
        plugin().getLogger().info("Registering native structures");
        plugin().getStructureRegistry().registerAll(
                StructurePillagerShip.class,
                StructureFishermanRaft.class
        );
    }

    public static void registerAll() {
        registerItems();
        registerNPCs();
        registerStructures();
        plugin().scheduleTask((final BukkitScheduler scheduler) ->
                scheduler.scheduleSyncDelayedTask(plugin(), AncientScrollsNative::postRegisterAll)
        );
    }
}
