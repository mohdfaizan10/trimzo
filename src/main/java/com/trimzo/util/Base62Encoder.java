package com.trimzo.util;

import org.springframework.stereotype.Component;

@Component
public class Base62Encoder {

    // 62 characters — URL safe!
    private static final String CHARACTERS =
            "abcdefghijklmnopqrstuvwxyz" +
                    "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
                    "0123456789";

    private static final int BASE = 62;

    /**
     * Numeric ID ko Base62 short code mein convert karenge
     * Example: 1 → "b", 100 → "bM"
     *
     * @param id Database se aaya auto-increment ID
     * @return Short alphanumeric code
     */
    public String encode(long id) {
        StringBuilder shortCode = new StringBuilder();

        while (id > 0) {
            // Remainder nikalo
            int remainder = (int) (id % BASE);
            // Remainder ko character mein convert karo
            shortCode.append(CHARACTERS.charAt(remainder));
            // ID ko divide karo
            id /= BASE;
        }

        // Reverse karo — correct order ke liye
        return shortCode.reverse().toString();
    }

    /**
     * Base62 short code ko numeric ID mein wapas convert karo
     * Example: "b" → 1, "bM" → 100
     *
     * @param shortCode Base62 encoded string
     * @return Original numeric ID
     */
    public long decode(String shortCode) {
        long id = 0;

        for (char character : shortCode.toCharArray()) {
            // Har character ka position nikalo
            id = id * BASE + CHARACTERS.indexOf(character);
        }

        return id;
    }
}