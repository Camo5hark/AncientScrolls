package com.andrewreedhall.ancientscrolls.util;

import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;

import java.util.HashSet;
import java.util.Set;

import static org.bukkit.block.Biome.*;
import static org.bukkit.block.Biome.BEACH;
import static org.bukkit.block.Biome.COLD_OCEAN;
import static org.bukkit.block.Biome.DEEP_COLD_OCEAN;
import static org.bukkit.block.Biome.DEEP_FROZEN_OCEAN;
import static org.bukkit.block.Biome.DEEP_LUKEWARM_OCEAN;
import static org.bukkit.block.Biome.FROZEN_OCEAN;
import static org.bukkit.block.Biome.FROZEN_RIVER;
import static org.bukkit.block.Biome.LUKEWARM_OCEAN;
import static org.bukkit.block.Biome.MANGROVE_SWAMP;
import static org.bukkit.block.Biome.MUSHROOM_FIELDS;
import static org.bukkit.block.Biome.RIVER;
import static org.bukkit.block.Biome.SNOWY_BEACH;
import static org.bukkit.block.Biome.STONY_SHORE;
import static org.bukkit.block.Biome.SWAMP;
import static org.bukkit.entity.EntityType.*;

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
}
