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
import com.andrewreedhall.ancientscrolls.item.scroll.ItemScroll;
import com.andrewreedhall.ancientscrolls.util.Randomizer;
import net.kyori.adventure.text.Component;
import net.minecraft.network.protocol.Packet;
import org.bukkit.*;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vex;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.andrewreedhall.ancientscrolls.AncientScrollsPlugin.plugin;

public abstract class AncientScrollsNPC extends AncientScrollsRegistry.Value {
    private static final String PMK_ANCIENT_SCROLLS_NPC_INSTANCE = "ancient_scrolls_npc_instance";
    private static final Comparator<Pair<Material, Integer>> ITEM_STACK_DESCRIPTOR_RANDOMIZER = new Randomizer<>();

    public final String name;
    public final NPCInstance.Skin skin;
    public final Function<NPCInstance, Packet<?>[]> auxiliary;
    public final Pair<Predicate<LivingEntity>, Double> generator;
    public final List<ItemScroll> resultScrolls;
    public final List<Pair<Material, Integer>> ingredientItemStackDescriptors;
    public final Component merchantName;

    public AncientScrollsNPC(
            final NamespacedKey key,
            final String name,
            final NPCInstance.Skin skin,
            final Function<NPCInstance, Packet<?>[]> auxiliary,
            final Pair<Predicate<LivingEntity>, Double> generator,
            final Set<String> resultScrollKeyStrings,
            final Set<Pair<Material, Integer>> ingredientItemStackDescriptors
    ) {
        super(key);
        this.name = name;
        this.skin = skin;
        this.auxiliary = auxiliary;
        this.generator = generator;
        this.resultScrolls = new ArrayList<>(
                resultScrollKeyStrings
                        .stream()
                        .map((final String resultScrollKeyString) ->
                                (ItemScroll) plugin().getItemRegistry().get(NamespacedKey.fromString(resultScrollKeyString))
                        )
                        .toList()
        );
        this.resultScrolls.sort(Randomizer.SCROLL_RANDOMIZER);
        this.ingredientItemStackDescriptors = new ArrayList<>(ingredientItemStackDescriptors.stream().toList());
        this.ingredientItemStackDescriptors.sort(ITEM_STACK_DESCRIPTOR_RANDOMIZER);
        this.merchantName = Component.text(this.name);
    }

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

    public static void cleanupInstance(final NPCInstance npcInstance) {
        final Player npcInstancePlayer = getPlayer(npcInstance);
        final World npcInstancePlayerWorld = npcInstancePlayer.getWorld();
        npcInstancePlayerWorld.getNearbyPlayers(npcInstancePlayer.getLocation(), 10.0).forEach((final Player nearbyPlayer) -> {
            if (nearbyPlayer.getOpenInventory().getTopInventory().getHolder() != null) {
                return;
            }
            nearbyPlayer.closeInventory();
        });
        final Location npcInstancePlayerEyeLocation = npcInstancePlayer.getEyeLocation();
        npcInstancePlayerWorld.spawn(npcInstancePlayerEyeLocation, Vex.class);
        npcInstancePlayerWorld.playSound(npcInstancePlayer.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0F, 1.0F);
        npcInstancePlayerWorld.spawnParticle(
                Particle.SOUL_FIRE_FLAME,
                npcInstancePlayerEyeLocation,
                100,
                1.0,
                1.0,
                1.0
        );
    }
}
