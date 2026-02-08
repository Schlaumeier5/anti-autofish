package de.schlaumeier;

import java.util.List;

public class ScoreCalculator {

    public static int calculate(FishStats s) {
        if (s.castAfterCatch.size() < 8) return 0;

        double avg = avg(s.castAfterCatch);
        double std = std(s.castAfterCatch, avg);

        int score = 0;

        if (avg < 300) score += 4;
        if (std < 50) score += 3;
        if (identicalCount(s.castAfterCatch) >= 8) score += 3;

        if (!s.dipToPull.isEmpty() && avg(s.dipToPull) < 200) score += 2;

        return Math.min(score, 10);
    }

    private static double avg(List<Long> l) {
        return l.stream().mapToLong(a -> a).average().orElse(0);
    }

    private static double std(List<Long> l, double avg) {
        return Math.sqrt(l.stream()
                .mapToDouble(a -> Math.pow(a - avg, 2))
                .average().orElse(0));
    }

    private static int identicalCount(List<Long> l) {
        if (l.size() < 2) return 0;
        int count = 1, max = 1;
        for (int i = 1; i < l.size(); i++) {
            if (Math.abs(l.get(i) - l.get(i - 1)) < 10) {
                count++;
                max = Math.max(max, count);
            } else count = 1;
        }
        return max;
    }

    public static int toPercent(int score) {
        return score * 10;
    }
}
