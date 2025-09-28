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

package com.andrewreedhall.ancientscrolls.npc;

import com.andrewreedhall.ancientscrolls.AncientScrollsRegistry;
import net.minecraft.network.protocol.Packet;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import oshi.util.tuples.Pair;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public abstract class AncientScrollsNPC extends AncientScrollsRegistry.Value {
    private static final String PMK_ANCIENT_SCROLLS_NPC_INSTANCE = "ancient_scrolls_npc_instance";

    public final String name;
    public final NPCInstance.Skin skin;
    public final Function<NPCInstance, Packet<?>[]> additionalAddInstanceToClientPacketBuilder;
    public final Pair<Predicate<LivingEntity>, Double> generator;

    public AncientScrollsNPC(
            final NamespacedKey key,
            final String name,
            final NPCInstance.Skin skin,
            final Function<NPCInstance, Packet<?>[]> additionalAddInstanceToClientPacketBuilder,
            final Pair<Predicate<LivingEntity>, Double> generator
    ) {
        super(key);
        this.name = name;
        this.skin = skin;
        this.additionalAddInstanceToClientPacketBuilder = additionalAddInstanceToClientPacketBuilder;
        this.generator = generator;
    }

    public abstract Merchant createInstanceMerchant();

    public NPCInstance createInstance(final World world, final Location location) {
        final NPCInstance npcInstance = new NPCInstance(
                this,
                ((CraftWorld) world).getHandle(),
                location.getX(),
                location.getY(),
                location.getZ()
        );
        getPlayer(npcInstance).setMetadata(PMK_ANCIENT_SCROLLS_NPC_INSTANCE, new FixedMetadataValue(plugin(), npcInstance));
        plugin().getNPCHandler().activeNPCInstances.add(npcInstance);
        return npcInstance;
    }

    public boolean generate(final LivingEntity spawnedLivingEntity) {
        if (!this.generator.getA().test(spawnedLivingEntity) ||
                plugin().getUniversalRandom().nextDouble() > this.generator.getB() * plugin().getDefaultCachedConfig().npc_generation_probabilityScalar
        ) {
            return false;
        }
        this.createInstance(spawnedLivingEntity.getWorld(), spawnedLivingEntity.getLocation());
        return true;
    }

    public static Player getPlayer(final NPCInstance npcInstance) {
        return (Player) plugin().getServer().getEntity(npcInstance.player.getUUID());
    }

    public static NPCInstance getInstance(final Entity entity) {
        if (!(entity instanceof Player instancePlayer) || !NPCInstance.is(((CraftEntity) entity).getHandle())) {
            return null;
        }
        final List<MetadataValue> instancePlayerNPCInstanceData = instancePlayer.getMetadata(PMK_ANCIENT_SCROLLS_NPC_INSTANCE);
        return instancePlayerNPCInstanceData.isEmpty() ? null : (NPCInstance) instancePlayerNPCInstanceData.getFirst().value();
    }

    public static void closeAllPossibleInstanceMerchantInventories() {
        plugin().getServer().getOnlinePlayers().forEach((final Player onlinePlayer) -> {
            final Inventory onlinePlayerOpenTopInventory = onlinePlayer.getOpenInventory().getTopInventory();
            if (!(onlinePlayerOpenTopInventory instanceof MerchantInventory) || onlinePlayerOpenTopInventory.getHolder() != null) {
                return;
            }
            onlinePlayer.closeInventory();
        });
    }
}
