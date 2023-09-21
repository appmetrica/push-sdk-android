package io.appmetrica.analytics.push.testutils;

import java.util.Random;

public final class Rand {

    private Rand() {
    }

    public static String randomString() {
        return randomString(10);
    }

    public static String randomString(int length) {
        return new RandomStringGenerator(length).nextString();
    }

    public static int randomInt() {
        return new Random().nextInt();
    }

    public static int randomInt(int bound) {
        return new Random().nextInt(bound);
    }

    public static int randomPositiveInt() {
        return randomInt(Integer.MAX_VALUE - 1) + 1;
    }

    public static int randomNegativeInt() {
        return -randomPositiveInt();
    }

    public static boolean randomBoolean() {
        return new Random().nextBoolean();
    }

    public static long randomLong() {
        return new Random().nextLong();
    }
}
