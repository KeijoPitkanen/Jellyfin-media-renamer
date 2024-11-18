# Jellyfin-Media-Renamer (JellyMR)
Current release is v1
This java program was created to rename downloaded or misnamed files to comply with the rules of Jellyfin for names of shows, movies and .srt subtitle files. If you have manually configured all of the shows to work with special suffixes it is not recommended to use this.

## What does this program do exactly:
1. Remove dots from file/directory names
2. Remove tags that are not imdb id tags
3. Paranthesises the year
4. Deletes "useless" files -> files that are .exe, .txt, .jpg or png.

!!!CURRENTLY THIS DELETES "USELESS" FILES LIKE PHOTOS AND .exe FILES WITHOUT ASKING FOR CONSENT!!!

JMR v1 does not support renaming .srt files
Original file name
A.Movie.Title.1968.1080p.BluRay.x264-[Torrent.site].mp4
After running JMR
A Movie Title (1968).mp4

## Known bugs v1
1. Doesn't work for tv-episodes if they include the year before episode name/index i.e. "Show (2020) S01E01" will not work. This is also against Jellyfin naming rules
2. If tv show is named in this way it will name one of the episodes by the shows name and not rename the rest of them.

## Goals for v2
1. Add support for tmdb and tvdbid id tags.
2. Fix show renaming bug
3. Add renaming possibility for .srt files
4. Get consent for deleting files


## The Jellyfin Project:
https://github.com/jellyfin/jellyfin
