package com.calc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class InputCheckTest {

    @Test
    void testRunAllParsers() {
        var ic = new InputCheck();
        /*|| TEST TITLES || 
         * Original
         * //Violet Evergarden [BD][1080p][HEVC 10bit x265][Dual Audio][Tenrai-Sensei]
         * Excepted
         * //Violet Evergarden
         * 
         * Original
         * //Tiny Toon Adventures (1990) Season 1-3 S01-S03 (480p Mixed x265 HEVC 10bit AAC 2.0 Ghost)
         * Expected
         * //Tiny Toon Adventures (1990)
         * 
         * Original
         * //Season 1
         * Expected
         * //Season 1
         * 
         * Original
         * //Violet Evergarden - S01E01 - 'I Love You' And Auto Memory Dolls.mkv
         * Expected
         * //Violet Evergarden - S01E01 - 'I love you' And Auto Memory Dolls.mkv
         * 
         * Original
         * //2001.A.Space.Odyssey.1968.1080p.BluRay.x264-[YTS.AM].mp4
         * Expected
         * //2001 A Space Odyssey (1968)
        */

        assertEquals("Violet Evergarden", ic.runAllParsers("Violet Evergarden[BD][1080p][HEVC 10bit x265][Dual Audio][Tenrai-Sensei] "));
        assertEquals("Tiny Toon Adventures (1990)", ic.runAllParsers("Tiny Toon Adventures (1990) Season 1-3 S01-S03 (480p Mixed x265 HEVC 10bit AAC 2.0 Ghost)"));
        assertEquals("Season 1", ic.runAllParsers("Season 1"));
        assertEquals("Violet Evergarden - S01E01 - 'I Love You' And Auto Memory Dolls.mkv", ic.runAllParsers("Violet Evergarden - S01E01 - 'I Love You' And Auto Memory Dolls.mkv"));
        assertEquals("2001 A Space Odyssey (1968).mp4",ic.runAllParsers("2001.A.Space.Odyssey.1968.1080p.BluRay.x264-[YTS.AM].mp4"));

    }


}
