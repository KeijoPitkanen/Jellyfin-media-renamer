# Jellyfin-Media-Renamer (JMR)
This java program was created to rename downloaded or misnamed files to comply with the rules of Jellyfin for names of shows, movies and .srt subtitle files. If you have manually configured all of the shows to work with special suffixes it is not recommended to use this.

## What does this program do exactly:
1. Remove dots from file/directory names
2. Remove tags that are not imdb id tags
3. Paranthesises the year
4. Deletes "useless" files -> files that are .exe, .txt, .jpg or png.

Original file name

A.Movie.Title.1968.1080p.BluRay.x264-[Torrent.site].mp4

After running JMR

A Movie Title (1968).mp4

JMR does not support renaming .srt files for now


## Known bugs v1.1
1. Doesn't work for tv-episodes if they include the year before episode name/index i.e. "Show (2020) S01E01" will not work. This is also against Jellyfin naming rules
2. If tv show is named in this way it will name one of the episodes by the shows name and not rename the rest of them.

## Goals for v1.2
1. Support for renaming .srt files correctly.
2. Better install format

## Possible future features

## The Jellyfin Project:
https://github.com/jellyfin/jellyfin
