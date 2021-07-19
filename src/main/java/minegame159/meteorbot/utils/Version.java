package minegame159.meteorbot.utils;

public class Version {
    private final int[] numbers;

    public Version(String string) {
        this.numbers = new int[3];

        String[] split = string.split("\\.");
        if (split.length != 3) throw new IllegalArgumentException("Version string needs to have 3 numbers.");

        for (int i = 0; i < 3; i++) {
            try {
                numbers[i] = Integer.parseInt(split[i]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Failed to parse version string.");
            }
        }
    }

    public Version increment() {
        numbers[2]++;

        if (numbers[2] > 9) {
            numbers[1]++;
            numbers[2] = 0;

            if (numbers[1] > 9) {
                numbers[0]++;
                numbers[1] = 0;
            }
        }

        return this;
    }

    @Override
    public String toString() {
        return String.format("%d.%d.%d", numbers[0], numbers[1], numbers[2]);
    }
}
