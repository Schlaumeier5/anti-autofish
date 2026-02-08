package de.schlaumeier;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.projectile.FishingHook;

import java.util.*;

public class FishTracker {
    private static final Map<UUID, FishStats> DATA = new HashMap<>();
    private static final Map<Integer, UUID> BOBBER_TO_PLAYER = new HashMap<>();
    private static final Set<Integer> KNOWN_BOBBERS = new HashSet<>();

    public static void tick(Minecraft mc) {
        if (mc.level == null) return;

        Set<Integer> currentBobbers = new HashSet<>();

        for (FishingHook bobber : mc.level.getEntitiesOfClass(
                FishingHook.class,
                mc.player.getBoundingBox().inflate(64))) {

            if (bobber.getPlayerOwner() == null) continue;

            int id = bobber.getId();
            UUID playerId = bobber.getPlayerOwner().getUUID();
            currentBobbers.add(id);

            FishStats stats = DATA.computeIfAbsent(playerId, k -> new FishStats());

            // === CAST ===
            if (!KNOWN_BOBBERS.contains(id)) {
                stats.markCast();
                BOBBER_TO_PLAYER.put(id, playerId);
            }

            // === BITE (Dip Detection) ===
            if (stats.state == FishState.CAST && bobber.getDeltaMovement().y < -0.04) {
                stats.markDip();
            }
        }

        // === CATCH / RESET ===
        for (int oldId : KNOWN_BOBBERS) {
            if (!currentBobbers.contains(oldId)) {
                UUID playerId = BOBBER_TO_PLAYER.get(oldId);
                if (playerId != null) {
                    FishStats stats = DATA.get(playerId);
                    if (stats != null) {
                        stats.markCatch();
                    }
                }
                BOBBER_TO_PLAYER.remove(oldId);
            }
        }

        KNOWN_BOBBERS.clear();
        KNOWN_BOBBERS.addAll(currentBobbers);
    }

    public static FishStats getStats(UUID player) {
        return DATA.get(player);
    }
}