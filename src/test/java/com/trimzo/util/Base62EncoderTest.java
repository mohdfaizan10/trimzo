package com.trimzo.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class Base62EncoderTest {

    private final Base62Encoder encoder = new Base62Encoder();

    @Test
    @DisplayName("Encoding ID 1 should return non-empty string")
    void encode_shouldReturnNonEmptyString() {
        String result = encoder.encode(1L);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("Encoding and decoding should return original ID")
    void encodeDecode_shouldBeReversible() {
        long original = 12345L;
        String encoded = encoder.encode(original);
        long decoded = encoder.decode(encoded);
        assertEquals(original, decoded);
    }

    @Test
    @DisplayName("Different IDs should produce different codes")
    void encode_differentIds_shouldProduceDifferentCodes() {
        String code1 = encoder.encode(1L);
        String code2 = encoder.encode(2L);
        assertNotEquals(code1, code2);
    }

    @Test
    @DisplayName("Encoded string should only contain Base62 characters")
    void encode_shouldOnlyContainValidCharacters() {
        String validChars =
                "abcdefghijklmnopqrstuvwxyz" +
                        "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                        "0123456789";

        String result = encoder.encode(99999L);
        for (char c : result.toCharArray()) {
            assertTrue(validChars.indexOf(c) >= 0,
                    "Invalid character found: " + c);
        }
    }

    @Test
    @DisplayName("Large ID should still encode correctly")
    void encode_largeId_shouldWorkCorrectly() {
        long largeId = 1_000_000L;
        String encoded = encoder.encode(largeId);
        assertEquals(largeId, encoder.decode(encoded));
    }
}