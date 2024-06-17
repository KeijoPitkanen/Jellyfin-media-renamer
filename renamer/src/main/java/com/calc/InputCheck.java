package com.calc;

public class InputCheck {

    //TODO: replace '.' with ' '

    //TODO: if the name has [] and the content inside the brackets are not imdb id delete the tag

    //TODO: after the name of the movie/show if there is a year put it inside ()

    //TODO: test
    protected String deleteResolutionTag(String input)   {
        String resolution = "420p";
        boolean state = true;

        while (state == true)   {
            if (input.contains(resolution)) {
                String temp = input;
                //This might cause problems
                temp.substring(input.indexOf(resolution) + resolution.length());
                input = input.substring(0, input.indexOf(resolution)) + temp;
            }

            switch (resolution) {
                case "420p":
                    resolution = "720p";
                    break;
                case "720p":
                    resolution = "1080p";
                    break;
                case "1080p":
                    resolution = "1440p";
                    break;
                default:
                    state = false;
                    break;
            }
        }
        return input;
    }

    //TODO: delete everything after (year) except [IMdb id] 
}
