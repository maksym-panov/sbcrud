package com.maksympanov.hneu.mjt.sbcrud;

import java.security.SecureRandom;

public class TestUtils {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private static final String DIGITS = "0123456789";

    private static final SecureRandom RND = new SecureRandom();

    public static String getRandomAlphabeticalString(int length) {
        var rv = new StringBuilder();
        RND.ints()
            .limit( length )
            .map( Math::abs )
            .map( d -> d % ALPHABET.length() )
            .forEach( d -> rv.append(ALPHABET.charAt(d)) );
        return rv.toString();
    }

    public static String getRandomNumericString(int length) {
        var rv = new StringBuilder();
        RND.ints()
                .limit( length )
                .map( Math::abs )
                .map( d -> d % DIGITS.length() )
                .forEach( d -> rv.append(DIGITS.charAt(d)) );
        return rv.toString();
    }

    public static int getRandomPositiveNumber() {
        var rv = Math.abs(RND.nextInt());
        return rv == 0 ? 1 : rv;
    }

    private TestUtils() {}

}
