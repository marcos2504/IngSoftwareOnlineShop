package com.example.onlineshop.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AddressTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Address getAddressSample1() {
        return new Address().id(1L).address1("address11").address2("address21").city("city1").postcode("postcode1").country("country1");
    }

    public static Address getAddressSample2() {
        return new Address().id(2L).address1("address12").address2("address22").city("city2").postcode("postcode2").country("country2");
    }

    public static Address getAddressRandomSampleGenerator() {
        return new Address()
            .id(longCount.incrementAndGet())
            .address1(UUID.randomUUID().toString())
            .address2(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .postcode(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString());
    }
}
