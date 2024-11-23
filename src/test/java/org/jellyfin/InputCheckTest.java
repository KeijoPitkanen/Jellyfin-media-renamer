package org.jellyfin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class InputCheckTest {

  @Test
  void testRunAllParsers() {
    System.out.println("Starting test on all parsers");
    var ic = new InputCheck();

    assertEquals("2001 A Space Odyssey (1968).mp4",
            ic.runAllParsers("2001.A.Space.Odyssey.1968.1080p.BluRay.x264-[YTS.AM].mp4", true, false));
    assertEquals("Violet Evergarden",
        ic.runAllParsers("Violet Evergarden[BD][1080p][HEVC 10bit x265][Dual Audio][Tenrai-Sensei] ", false, false));
    assertEquals("Tiny Toon Adventures (1990)",
        ic.runAllParsers("Tiny Toon Adventures (1990) Season 1 S01 [tgmovies](480p Mixed x265 HEVC 10bit AAC 2.0 Ghost)", false, false));
    assertEquals("Season 1",
            ic.runAllParsers("Season 1", false, false));
    assertEquals("Violet Evergarden - S01E01 - 'I Love You' And Auto Memory Dolls.mkv",
        ic.runAllParsers("Violet Evergarden - S01E01 - 'I Love You' And Auto Memory Dolls.mkv", true, true));
    assertEquals("Austin Powers (2001).mp4",
            ic.runAllParsers("Austin Powers 2001.mp4", true, false));
    assertEquals("Indiana jones (2018) [tt7562112]",
            ic.runAllParsers("Indiana jones (2018) [tt7562112] [hdtv]", false, false));
    assertEquals("American Horror Story - S03E01 - Bitchcraft.mkv",
            ic.runAllParsers("American Horror Story (2011) - S03E01 - Bitchcraft (1080p BluRay x265 RZeroX).mkv", true, true));
    assertEquals("Movie (2018) [tmdbid-7562112]",
            ic.runAllParsers("Movie (2018) [tmdbid-7562112] [hdtv]", false, false));
    assertEquals("Movie (2018) [tvdbid-7562112]",
            ic.runAllParsers("Movie (2018) [tvdbid-7562112] [hdtv]", false, false));
    System.out.println("All tests have been run");

  }

}
