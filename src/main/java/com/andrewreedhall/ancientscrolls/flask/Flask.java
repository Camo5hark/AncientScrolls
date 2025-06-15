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

package com.andrewreedhall.ancientscrolls.flask;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public enum Flask {
    LAUDANUM(
            ChatColor.DARK_PURPLE + "Laudanum",
            "An ancient pain-relieving supplement",
            Color.BLACK,
            PotionEffectType.REGENERATION,
            PotionEffectType.SLOWNESS
    ),
    UNSTABLE_CONCOCTION(
            ChatColor.AQUA + "Unstable Concoction",
            "Energy boost at a cost",
            Color.ORANGE,
            PotionEffectType.SPEED,
            PotionEffectType.WEAKNESS
    ),
    HERBAL_MIXTURE(
            ChatColor.GREEN + "Herbal Mixture",
            "Composed of spring flowers and insects",
            Color.GREEN,
            PotionEffectType.JUMP_BOOST,
            PotionEffectType.INFESTED
    ),
    DRAGONS_BLOOD(
            ChatColor.GOLD + "Dragon's Blood",
            "Ancient dragons were born in fire",
            Color.RED,
            PotionEffectType.FIRE_RESISTANCE,
            PotionEffectType.BLINDNESS
    );

    private final String title;
    private final List<String> cachedLore;
    private final Color color;
    private final PotionEffectType primaryEffectType;
    private final PotionEffectType secondaryEffectType;

    Flask(String title, String description, Color color, PotionEffectType primaryEffectType, PotionEffectType secondaryEffectType) {
        this.title = title;
        this.cachedLore = List.of(ChatColor.GRAY + ChatColor.ITALIC.toString() + description);
        this.color = color;
        this.primaryEffectType = primaryEffectType;
        this.secondaryEffectType = secondaryEffectType;
    }

    public ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.POTION);
        PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
        if (potionMeta == null) {
            throw new NullPointerException("potionMeta == null");
        }
        potionMeta.setDisplayName(this.title);
        potionMeta.setLore(this.cachedLore);
        potionMeta.addEnchant(Enchantment.UNBREAKING, 1, false);
        potionMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        potionMeta.setColor(this.color);
        potionMeta.addCustomEffect(new PotionEffect(this.primaryEffectType, 400, 1, false, true, false), false);
        potionMeta.addCustomEffect(new PotionEffect(this.secondaryEffectType, 200, 0, false, true, false), false);
        itemStack.setItemMeta(potionMeta);
        return itemStack;
    }
}
