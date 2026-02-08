package de.schlaumeier;

import java.util.ArrayList;
import java.util.List;

public class FishStats {

    public long lastCatchTime = -1;
    public long lastCastTime = -1;
    public long lastDipTime = -1;
    public long lastSeenBobber = -1;

    public final List<Long> castAfterCatch = new ArrayList<>();
    public final List<Long> dipToPull = new ArrayList<>();

    public void markCatch(long now) {
        if (lastDipTime > 0) {
            dipToPull.add(now - lastDipTime);
        }
        lastCatchTime = now;
    }

    public void markCast(long now) {
        if (lastCatchTime > 0) {
            castAfterCatch.add(now - lastCatchTime);
        }
        lastCastTime = now;
    }

    public void markDip() {
        if (lastDipTime < System.currentTimeMillis() - 2000) {
            lastDipTime = System.currentTimeMillis();
        }
    }

    public int getAutoFishingProbabilityPercent() {
        return ScoreCalculator.toPercent(ScoreCalculator.calculate(this));
    }
}
