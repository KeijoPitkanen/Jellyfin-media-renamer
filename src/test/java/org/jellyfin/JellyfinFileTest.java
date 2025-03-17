package org.jellyfin;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class JellyfinFileTest {
    @Test
    void test() {
        System.out.println("Starting test on renaming files");
        ArrayList<JellyfinFile> files = new ArrayList<>();
        ArrayList<String> fileNames = new ArrayList<>();

        fileNames.add("/mnt/Juuso Vault/main/Test/2001.A.Space.Odyssey.1968.1080p.BluRay.x264-[YTS.AM]/2001.A.Space.Odyssey.1968.1080p.BluRay.x264-[YTS.AM].mp4");
        fileNames.add("/mnt/Juuso Vault/main/Test/Violet Evergarden[BD][1080p][HEVC 10bit x265][Dual Audio][Tenrai-Sensei] ");
        fileNames.add("/mnt/Juuso Vault/main/Test/Tiny Toon Adventures (1990) Season 1 S01 [tgmovies](480p Mixed x265 HEVC 10bit AAC 2.0 Ghost)");
        fileNames.add("/mnt/Juuso Vault/main/Test/Season 1");
        fileNames.add("/mnt/Juuso Vault/main/Test/Violet Evergarden/Season 1/Violet Evergarden - S01E01 - 'I Love You' And Auto Memory Dolls.mkv");
        fileNames.add("/mnt/Juuso Vault/main/Test/Austin Powers 2001.mp4");
        fileNames.add("/mnt/Juuso Vault/main/Test/Indiana jones (2018) [tt7562112] [hdtv]");
        fileNames.add("/mnt/Juuso Vault/main/Test/American Horror Story/Season 01/American Horror Story (2011) - S03E01 - Bitchcraft (1080p BluRay x265 RZeroX).mkv");
        fileNames.add("/mnt/Juuso Vault/main/Test/Movie (2018) [tvdbid-7562112] [hdtv]");

        for (String s : fileNames)  {
            JellyfinFile file = new JellyfinFile(s);
            files.add(file);
        }

        for (JellyfinFile file : files) {
            file.renameToJellyfinFormat();
            
        }
    }

}
