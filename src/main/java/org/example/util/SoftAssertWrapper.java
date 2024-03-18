package org.example.util;

import org.assertj.core.api.SoftAssertions;

public class SoftAssertWrapper extends SoftAssertions {

    private static final ThreadLocal<SoftAssertWrapper> instance = new ThreadLocal<>();

    public static SoftAssertWrapper get() {
        if (instance.get() == null) {
            instance.set(new SoftAssertWrapper());
        }
        return instance.get();
    }

    @Override
    public void assertAll() {
        try {
            super.assertAll();
        }
        finally {
            instance.remove();
        }
    }
}
