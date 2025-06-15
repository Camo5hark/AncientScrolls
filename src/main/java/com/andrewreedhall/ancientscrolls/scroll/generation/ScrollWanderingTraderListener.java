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

package com.andrewreedhall.ancientscrolls.scroll.generation;

import com.andrewreedhall.ancientscrolls.scroll.Scroll;
import com.andrewreedhall.ancientscrolls.scroll.ScrollManager;
import org.bukkit.Material;
import org.bukkit.entity.WanderingTrader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ScrollWanderingTraderListener implements Listener {
    public ScrollWanderingTraderListener() {}

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (!(event.getInventory() instanceof MerchantInventory merchantInventory) || !(merchantInventory.getMerchant() instanceof WanderingTrader wanderingTrader)) {
            return;
        }
        List<MerchantRecipe> merchantRecipes = new ArrayList<>(wanderingTrader.getRecipes());
        merchantRecipes.add(generateRandomScrollRecipe(wanderingTrader));
        wanderingTrader.setRecipes(merchantRecipes);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getInventory() instanceof MerchantInventory merchantInventory) || !(merchantInventory.getMerchant() instanceof WanderingTrader wanderingTrader)) {
            return;
        }
        List<MerchantRecipe> merchantRecipes = new ArrayList<>(wanderingTrader.getRecipes());
        merchantRecipes.removeIf((MerchantRecipe merchantRecipe) -> ScrollManager.getRegisteredScroll(merchantRecipe.getResult()) != null);
        wanderingTrader.setRecipes(merchantRecipes);
    }

    private static MerchantRecipe generateRandomScrollRecipe(WanderingTrader wanderingTrader) {
        Object[] scrolls = ScrollManager.getRegisteredScrolls().toArray();
        Random random = new Random(wanderingTrader.getSeed());
        MerchantRecipe merchantRecipe = new MerchantRecipe(((Scroll) scrolls[random.nextInt(scrolls.length)]).createItemStack(), 1);
        merchantRecipe.addIngredient(new ItemStack(Material.EMERALD, random.nextInt(28, 49)));
        merchantRecipe.addIngredient(new ItemStack(Material.ANCIENT_DEBRIS));
        return merchantRecipe;
    }
}
