package com.example.onlineshop.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class WishListTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static WishList getWishListSample1() {
        return new WishList().id(1L).title("title1");
    }

    public static WishList getWishListSample2() {
        return new WishList().id(2L).title("title2");
    }

    public static WishList getWishListRandomSampleGenerator() {
        return new WishList().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString());
    }
}
