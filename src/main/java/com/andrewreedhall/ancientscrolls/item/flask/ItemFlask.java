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
import com.andrewreedhall.ancientscrolls.util.BukkitUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public abstract class ItemFlask extends AncientScrollsItem {
    private final Component displayName;
    private final List<Component> cachedLore;
    private final Color color;
    private final PotionEffect buffPotionEffect;
    private final PotionEffect debuffPotionEffect;

    public ItemFlask(
            final NamespacedKey key,
            final String displayName,
            final NamedTextColor displayNameColor,
            final String[] lore,
            final Color color,
            final PotionEffectType buffPotionEffectType,
            final PotionEffectType debuffPotionEffectType
    ) {
        super(key);
        this.displayName = Component.text(displayName, displayNameColor);
        this.cachedLore = new ArrayList<>(lore.length);
        for (String loreElem : lore) {
            this.cachedLore.add(Component.text(loreElem, NamedTextColor.GRAY, TextDecoration.ITALIC));
        }
        this.color = color;
        this.buffPotionEffect = new PotionEffect(buffPotionEffectType, 1200, 1, false);
        this.debuffPotionEffect = new PotionEffect(debuffPotionEffectType, 100, 0, false);
        this.putMCLootTableGenProb("blocks/trial_spawner", 0.05);
        this.putMCLootTableGenProb("blocks/vault", 0.24);
    }

    @Override
    public ItemStack createItemStack(final int amount) {
        ItemStack itemStack = new ItemStack(Material.POTION);
        ItemMeta itemMeta = BukkitUtil.getItemMeta(itemStack);
        PotionMeta potionMeta = (PotionMeta) itemMeta;
        potionMeta.displayName(this.displayName);
        potionMeta.lore(this.cachedLore);
        potionMeta.setColor(this.color);
        potionMeta.setEnchantmentGlintOverride(true);
        potionMeta.addCustomEffect(this.buffPotionEffect, false);
        potionMeta.addCustomEffect(this.debuffPotionEffect, false);
        itemStack.setItemMeta(potionMeta);
        BukkitUtil.hidePotionContentsTooltipDisplay(itemStack);
        return itemStack;
    }
}
