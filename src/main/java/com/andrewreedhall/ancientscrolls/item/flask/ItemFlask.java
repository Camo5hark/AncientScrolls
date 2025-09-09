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
        this.buffPotionEffect = new PotionEffect(buffPotionEffectType, 1200, 1);
        this.debuffPotionEffect = new PotionEffect(debuffPotionEffectType, 300, 0);
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
        potionMeta.addEnchant(Enchantment.UNBREAKING, 1, false);
        potionMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        potionMeta.addCustomEffect(this.buffPotionEffect, false);
        potionMeta.addCustomEffect(this.debuffPotionEffect, false);
        itemStack.setItemMeta(potionMeta);
        return itemStack;
    }
}
