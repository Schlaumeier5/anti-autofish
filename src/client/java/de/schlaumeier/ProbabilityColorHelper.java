package de.schlaumeier;


public class ProbabilityColorHelper {
    private static int getRed(int percentage) {
        return Math.min(255, percentage * 128 / 100);
    }
    private static int getGreen(int percentage) {
        return Math.min(255, (100 - percentage) * 128 / 100);
    }
    private static int getBlue(int percentage) {
        return 0;
    }
    public static int getColor(int percentage) {
        int color = 0xFF000000;
        color += getRed(percentage) * 0x00010000;
        color += getGreen(percentage) * 0x00000100;
        color += getBlue(percentage);
        return color;
    }
}
