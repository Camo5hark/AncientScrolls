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

package com.andrewreedhall.ancientscrolls.util;

import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;

import static org.bukkit.block.Biome.*;
import static org.bukkit.entity.EntityType.*;
import static org.bukkit.potion.PotionEffectType.*;
import static org.bukkit.Material.*;

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
    public static final Set<Material> MELEE_WEAPON_TYPES = Set.of(
            WOODEN_SWORD,
            STONE_SWORD,
            IRON_SWORD,
            GOLDEN_SWORD,
            DIAMOND_SWORD,
            NETHERITE_SWORD,

            WOODEN_AXE,
            STONE_AXE,
            IRON_AXE,
            GOLDEN_AXE,
            DIAMOND_AXE,
            NETHERITE_AXE,

            MACE
    );
}
