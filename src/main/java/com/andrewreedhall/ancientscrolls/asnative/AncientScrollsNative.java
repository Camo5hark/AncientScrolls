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
                ScrollPestControl.class,
                ScrollArchaicSkincareRoutine.class, // BOSS: WITHER
                ScrollToxicology.class,

                // effect/night vision
                ScrollAquaticLensing.class,
                ScrollMeteorology.class,
                ScrollCanineStudies.class,
                ScrollSpelunking.class,
                ScrollHillclimbing.class,

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

                // end city
                ScrollSniping.class,
                ScrollQuantumTunnelling.class,
                ScrollDurability.class,
                ScrollNightLife.class, // TODO ender dragon drop?

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
