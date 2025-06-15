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

package com.andrewreedhall.ancientscrolls.trimbonus;

import com.andrewreedhall.ancientscrolls.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimPattern;

import java.util.*;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

/**
 * Armor with armor trim bonuses provide wearer with resistance and strength against certain mobs
 */
public enum TrimBonus {
    SENTRY(
            TrimPattern.SENTRY,
            Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE,

            EntityType.PILLAGER,
            EntityType.VINDICATOR,
            EntityType.RAVAGER
    ),
    VEX(
            TrimPattern.VEX,
            Material.VEX_ARMOR_TRIM_SMITHING_TEMPLATE,

            EntityType.VINDICATOR,
            EntityType.EVOKER,
            EntityType.VEX
    ),
    WILD(
            TrimPattern.WILD,
            Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE,

            EntityType.CAVE_SPIDER,
            EntityType.BOGGED
    ),
    COAST(
            TrimPattern.COAST,
            Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE,

            EntityType.DROWNED
    ),
    DUNE(
            TrimPattern.DUNE,
            Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE,

            EntityType.HUSK
    ),
    WARD(
            TrimPattern.WARD,
            Material.WARD_ARMOR_TRIM_SMITHING_TEMPLATE,

            EntityType.WARDEN
    ),
    SILENCE(
            TrimPattern.SILENCE,
            Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE,

            EntityType.WARDEN,
            EntityType.WITHER
    ),
    SNOUT(
            TrimPattern.SNOUT,
            Material.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE,

            EntityType.PIGLIN,
            EntityType.PIGLIN_BRUTE,
            EntityType.HOGLIN
    ),
    RIB(
            TrimPattern.RIB,
            Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE,

            EntityType.BLAZE,
            EntityType.WITHER_SKELETON,
            EntityType.SKELETON
    ),
    EYE(
            TrimPattern.EYE,
            Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE,

            EntityType.ENDERMAN
    ),
    SPIRE(
            TrimPattern.SPIRE,
            Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE,

            EntityType.SHULKER,
            EntityType.ENDER_DRAGON
    ),
    FLOW(
            TrimPattern.FLOW,
            Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE,

            EntityType.BREEZE,
            EntityType.SLIME,
            EntityType.SILVERFISH
    ),
    BOLT(
            TrimPattern.BOLT,
            Material.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE,

            EntityType.BREEZE
    ),
    WAYFINDER(
            TrimPattern.WAYFINDER,
            Material.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE,

            EntityType.ZOMBIE,
            EntityType.SKELETON,
            EntityType.PHANTOM
    ),
    RAISER(
            TrimPattern.RAISER,
            Material.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE,

            EntityType.ZOMBIE,
            EntityType.SKELETON,
            EntityType.PHANTOM
    ),
    SHAPER(
            TrimPattern.SHAPER,
            Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE,

            EntityType.ZOMBIE,
            EntityType.SKELETON,
            EntityType.PHANTOM
    ),
    HOST(
            TrimPattern.HOST,
            Material.HOST_ARMOR_TRIM_SMITHING_TEMPLATE,

            EntityType.ZOMBIE,
            EntityType.SKELETON,
            EntityType.PHANTOM
    ),
    TIDE(
            TrimPattern.TIDE,
            Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE,

            EntityType.GUARDIAN,
            EntityType.DROWNED
    );

    public static final Map<Material, TrimBonus> TEMPLATE_TYPE_TO_BONUS;
    public static final Map<TrimPattern, TrimBonus> PATTERN_TO_BONUS;

    static {
        Map<Material, TrimBonus> templateTypeToBonus = new TreeMap<>();
        Map<TrimPattern, TrimBonus> patternToBonus = new HashMap<>();
        for (TrimBonus trimBonus : values()) {
            templateTypeToBonus.put(trimBonus.templateType, trimBonus);
            patternToBonus.put(trimBonus.pattern, trimBonus);
        }
        TEMPLATE_TYPE_TO_BONUS = Collections.unmodifiableMap(templateTypeToBonus);
        PATTERN_TO_BONUS = Collections.unmodifiableMap(patternToBonus);
    }

    public final TrimPattern pattern;
    public final Material templateType;
    public final Set<EntityType> mobTypes;
    public final List<String> itemStackLore;

    TrimBonus(TrimPattern pattern, Material templateType, EntityType... mobTypes) {
        this.pattern = pattern;
        this.templateType = templateType;
        this.mobTypes = Set.of(mobTypes);
        this.itemStackLore = this.createItemStackLore();
    }

    private List<String> createItemStackLore() {
        List<String> itemStackLore = new ArrayList<>();
        itemStackLore.add(ChatColor.GREEN + "-" + ((int) plugin().getCachedConfig().trimBonus_damageReductionPercentage) + "%" + ChatColor.GRAY + " damage taken from and");
        itemStackLore.add(ChatColor.GREEN + "+" + ((int) plugin().getCachedConfig().trimBonus_damageInflictionPercentage) + "%" + ChatColor.GRAY + " damage inflicted on");
        for (EntityType mobType : mobTypes) {
            itemStackLore.add(ChatColor.BLUE + " " + Utils.getEntityNameByType(mobType));
        }
        return Collections.unmodifiableList(itemStackLore);
    }

    public void setItemStackLore(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return;
        }
        itemMeta.setLore(this.itemStackLore);
        itemStack.setItemMeta(itemMeta);
    }

    public static TrimBonus getArmorItemStackBonus(ItemStack armorItemStack) {
        if (!(armorItemStack.getItemMeta() instanceof ArmorMeta armorItemMeta)) {
            return null;
        }
        ArmorTrim armorTrim = armorItemMeta.getTrim();
        if (armorTrim == null) {
            return null;
        }
        return PATTERN_TO_BONUS.get(armorTrim.getPattern());
    }

    public static boolean doesPlayerHaveBonusAgainst(Player player, EntityType mobType) {
        ItemStack[] wornArmorItemStacks = player.getInventory().getArmorContents();
        for (ItemStack wornArmorItemStack : wornArmorItemStacks) {
            if (wornArmorItemStack == null) {
                continue;
            }
            TrimBonus trimBonus = getArmorItemStackBonus(wornArmorItemStack);
            if (trimBonus == null) {
                continue;
            }
            if (trimBonus.mobTypes.contains(mobType)) {
                return true;
            }
        }
        return false;
    }
}
