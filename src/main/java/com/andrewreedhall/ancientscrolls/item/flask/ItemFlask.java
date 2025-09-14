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

package com.andrewreedhall.ancientscrolls.item.flask;

import com.andrewreedhall.ancientscrolls.item.AncientScrollsItem;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;
import static org.bukkit.ChatColor.*;

public abstract class ItemFlask extends AncientScrollsItem {
    private final String displayName;
    private final List<String> cachedLore;
    private final Color color;
    private final PotionEffect buffPotionEffect;
    private final PotionEffect debuffPotionEffect;

    public ItemFlask(final NamespacedKey key, final String displayName, final String[] lore, final Color color, final PotionEffectType buffPotionEffectType, final PotionEffectType debuffPotionEffectType) {
        super(key);
        this.displayName = displayName;
        this.cachedLore = new ArrayList<>(lore.length);
        for (String loreElem : lore) {
            this.cachedLore.add(GRAY + ITALIC.toString() + loreElem);
        }
        this.color = color;
        this.buffPotionEffect = new PotionEffect(buffPotionEffectType, 1200, 1, false);
        this.debuffPotionEffect = new PotionEffect(debuffPotionEffectType, 300, 0, false);
        this.putMCLootTableGenProb("blocks/trial_spawner", 0.05);
        this.putMCLootTableGenProb("blocks/vault", 0.24);
    }

    @Override
    public ItemStack createItemStack(final int amount) {
        ItemStack itemStack = new ItemStack(Material.POTION);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            plugin().getLogger().warning("ItemMeta is null for ItemFlask ItemStack");
            return itemStack;
        }
        PotionMeta potionMeta = (PotionMeta) itemMeta;
        potionMeta.setDisplayName(this.displayName);
        potionMeta.setLore(this.cachedLore);
        potionMeta.setColor(this.color);
        potionMeta.setEnchantmentGlintOverride(true);
        potionMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        potionMeta.addCustomEffect(this.buffPotionEffect, false);
        potionMeta.addCustomEffect(this.debuffPotionEffect, false);
        itemStack.setItemMeta(potionMeta);
        return itemStack;
    }
}
