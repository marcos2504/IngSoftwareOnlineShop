package com.example.onlineshop.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProductTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Product getProductSample1() {
        return new Product().id(1L).title("title1").keywords("keywords1").description("description1").rating(1);
    }

    public static Product getProductSample2() {
        return new Product().id(2L).title("title2").keywords("keywords2").description("description2").rating(2);
    }

    public static Product getProductRandomSampleGenerator() {
        return new Product()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .keywords(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .rating(intCount.incrementAndGet());
    }
}
