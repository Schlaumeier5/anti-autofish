package de.schlaumeier;

import java.util.ArrayList;
import java.util.List;

public class FishStats {
    public FishState state = FishState.IDLE;

    public long lastCatchTime = -1;
    public long lastCastTime = -1;
    public long lastDipTime = -1;
    public long lastSeenBobber = -1;

    public final List<Long> castAfterCatch = new ArrayList<>();
    public final List<Long> dipToPull = new ArrayList<>();

    public void markCatch(long now) {
        if (state == FishState.BITE) {
            state = FishState.IDLE;
            if (lastDipTime > 0) {
                dipToPull.add(now - lastDipTime);
            }
            lastCatchTime = now;
        }
    }
    public void markCatch() {
        markCatch(System.currentTimeMillis());
    }

    public void markCast(long now) {
        if (state == FishState.IDLE) {
            if (lastCatchTime > 0) {
                castAfterCatch.add(now - lastCatchTime);
            }
            lastCastTime = now;
        }
    }
    public void markCast() {
        markCast(System.currentTimeMillis());
    }

    public void markDip() {
        if (state == FishState.CAST) {
            state = FishState.BITE;
            if (lastDipTime < System.currentTimeMillis() - 2000) {
                lastDipTime = System.currentTimeMillis();
            }
        }
    }

    public int getAutoFishingProbabilityPercent() {
        return ScoreCalculator.toPercent(ScoreCalculator.calculate(this));
    }
}
