package com.trimzo.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class ReferrerParserTest {

    private final ReferrerParser parser = new ReferrerParser();

    @Test
    @DisplayName("Null referrer should return Direct")
    void parse_nullReferrer_shouldReturnDirect() {
        assertEquals("Direct", parser.parse(null));
    }

    @Test
    @DisplayName("Empty referrer should return Direct")
    void parse_emptyReferrer_shouldReturnDirect() {
        assertEquals("Direct", parser.parse(""));
    }

    @Test
    @DisplayName("Twitter URL should return Twitter")
    void parse_twitterUrl_shouldReturnTwitter() {
        assertEquals("Twitter",
                parser.parse("https://twitter.com/home"));
    }

    @Test
    @DisplayName("Facebook URL should return Facebook")
    void parse_facebookUrl_shouldReturnFacebook() {
        assertEquals("Facebook",
                parser.parse("https://www.facebook.com/feed"));
    }

    @Test
    @DisplayName("WhatsApp referrer should return WhatsApp")
    void parse_whatsappReferrer_shouldReturnWhatsApp() {
        assertEquals("WhatsApp",
                parser.parse("https://web.whatsapp.com"));
    }

    @Test
    @DisplayName("Unknown URL should return Other")
    void parse_unknownUrl_shouldReturnOther() {
        assertEquals("Other",
                parser.parse("https://www.somerandomblog.com"));
    }

    @Test
    @DisplayName("Google URL should return Google")
    void parse_googleUrl_shouldReturnGoogle() {
        assertEquals("Google",
                parser.parse("https://www.google.com/search"));
    }
}