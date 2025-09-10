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
                plugin().getEquippedScrollsInventoryHandler()
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

                // end city
                ScrollSniping.class,
                ScrollQuantumTunnelling.class,
                ScrollDurability.class,
                ScrollNightLife.class, // BOSS: ENDER DRAGON

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
