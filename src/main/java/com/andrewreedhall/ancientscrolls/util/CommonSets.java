package com.andrewreedhall.ancientscrolls.util;

import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;

import static org.bukkit.block.Biome.*;
import static org.bukkit.entity.EntityType.*;
import static org.bukkit.potion.PotionEffectType.*;

public final class CommonSets {
    public static final Set<EntityType> NETHER_MONSTERS = Set.of(
            BLAZE,
            GHAST,
            HOGLIN,
            MAGMA_CUBE,
            PIGLIN,
            PIGLIN_BRUTE,
            SKELETON,
            WITHER_SKELETON,
            ZOGLIN,
            ZOMBIFIED_PIGLIN
    );
    public static final Set<EntityType> UNDEAD_MONSTERS = Set.of(
            BOGGED,
            DROWNED,
            HUSK,
            PHANTOM,
            SKELETON,
            SKELETON_HORSE,
            STRAY,
            WITHER_SKELETON,
            ZOGLIN,
            ZOMBIE,
            ZOMBIE_HORSE,
            ZOMBIE_VILLAGER,
            ZOMBIFIED_PIGLIN
    );
    public static final Set<EntityType> OVERWORLD_MONSTERS = Set.of(
            BOGGED,
            BREEZE,
            CAVE_SPIDER,
            CREAKING,
            CREEPER,
            DROWNED,
            ELDER_GUARDIAN,
            ENDERMAN,
            EVOKER,
            GUARDIAN,
            HUSK,
            PHANTOM,
            PILLAGER,
            RAVAGER,
            SILVERFISH,
            SKELETON,
            SLIME,
            SPIDER,
            STRAY,
            VEX,
            VINDICATOR,
            WARDEN,
            WITCH,
            ZOMBIE
    );
    public static final Set<EntityType> ILLAGERS = Set.of(
            EVOKER,
            PILLAGER,
            VINDICATOR,
            RAVAGER,
            VEX,
            WITCH
    );
    public static final Set<Biome> AQUATIC_BIOMES = Set.of(
            OCEAN,
            DEEP_OCEAN,
            WARM_OCEAN,
            LUKEWARM_OCEAN,
            DEEP_LUKEWARM_OCEAN,
            COLD_OCEAN,
            DEEP_COLD_OCEAN,
            FROZEN_OCEAN,
            DEEP_FROZEN_OCEAN,
            MUSHROOM_FIELDS,

            RIVER,
            FROZEN_RIVER,
            SWAMP,
            MANGROVE_SWAMP,
            BEACH,
            SNOWY_BEACH,
            STONY_SHORE
    );
    public static final Set<Biome> HIGHLAND_BIOMES = Set.of(
            JAGGED_PEAKS,
            FROZEN_PEAKS,
            STONY_PEAKS,
            MEADOW,
            CHERRY_GROVE,
            GROVE,
            SNOWY_SLOPES,
            WINDSWEPT_HILLS,
            WINDSWEPT_GRAVELLY_HILLS,
            WINDSWEPT_FOREST
    );
    public static final Set<PotionEffectType> BAD_POTION_EFFECT_TYPES = Set.of(
            SLOWNESS,
            MINING_FATIGUE,
            INSTANT_DAMAGE,
            NAUSEA,
            BLINDNESS,
            HUNGER,
            WEAKNESS,
            POISON,
            PotionEffectType.WITHER,
            LEVITATION,
            WIND_CHARGED,
            WEAVING,
            OOZING,
            INFESTED,
            UNLUCK
    );
}
