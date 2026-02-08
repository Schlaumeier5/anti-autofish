package de.schlaumeier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.projectile.FishingHook;

public class FishTracker {
    private static final Map<UUID, FishStats> DATA = new HashMap<>();
    private static final Set<Integer> KNOWN_BOBBERS = new HashSet<>();

    public static void tick(Minecraft minecraft) {
        Set<Integer> currentBobbers = new HashSet<>();

        for (FishingHook bobber : minecraft.level.getEntitiesOfClass(FishingHook.class, minecraft.player.getBoundingBox().expandTowards(64, 64, 64))) {
            if (bobber.getPlayerOwner() != null) {
                int id = bobber.getId();
                currentBobbers.add(id);

                UUID playerId = bobber.getPlayerOwner().getUUID();
                FishStats stats = DATA.computeIfAbsent(playerId, k -> new FishStats());

                stats.lastSeenBobber = System.currentTimeMillis();

                // dip recognition (secondary)
                if (bobber.getDeltaMovement().y < -0.04) {
                    stats.markDip();
                }
            }
        }

        // Bobber verschwunden → Catch
        for (int oldId : KNOWN_BOBBERS) {
            if (!currentBobbers.contains(oldId)) {
                long now = System.currentTimeMillis();
                DATA.values().forEach(stats -> stats.markCatch(now));
            }
        }

        KNOWN_BOBBERS.clear();
        KNOWN_BOBBERS.addAll(currentBobbers);
    }

    public static FishStats getStats(UUID player) {
        return DATA.get(player);
    }
}