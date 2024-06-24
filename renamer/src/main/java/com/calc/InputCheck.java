package com.calc;

public class InputCheck {

    //TODO: test
    protected String changeDotsToSpaces(String input)   {
        input.replace('.', ' ');
        return input;
    }

    //TODO: test
    protected String removeNonimdbTags(String input)  {
        int startIndex = 1;
<<<<<<< HEAD
        int endIndex = 2;
=======
        int endIndex = 0;
>>>>>>> 3f5be8b8ae295ad3eeced047fdb0edcf94330bf7
        StringBuilder temp1 = new StringBuilder(input);
            
        //This code is not efficient but should work for now
        while (temp1.indexOf("[") != temp1.lastIndexOf("[")) {
            startIndex = temp1.indexOf("[");
            //checks for imdb id tag "[tt"
            if (temp1.substring(startIndex, startIndex + 3).equals("[tt") == true) {
                startIndex = temp1.indexOf("]");
                //If tag exists checks if there are other brackets and deletes them from the string builder
                if (temp1.substring(startIndex).indexOf("[") != -1) {
                    startIndex = temp1.substring(startIndex).indexOf("[");
                    endIndex = temp1.substring(startIndex).indexOf("]");
                    temp1.delete(startIndex, endIndex + 1);
                }
            }   else    {
                startIndex = temp1.substring(startIndex).indexOf("[");
                endIndex = temp1.substring(startIndex).indexOf("]");
                temp1.delete(startIndex, endIndex + 1);   
            }    
        }
        input = temp1.toString();
        return input;
    }

    //TODO: test
    protected String parenthesisTheYear(String input)   {
        int startIndex = 1;
        int endIndex = 2;
        StringBuilder temp = new StringBuilder(input);

        //This is just a mess
        if (input.substring(startIndex, endIndex) == "19" || input.substring(startIndex, endIndex) == "20") {
            if (checkForSpecifiedChar(input, startIndex) == true) {
                if (Character.isDigit(input.charAt(endIndex)) == true && Character.isDigit(input.charAt(endIndex + 1)) == true) {
                    if (Character.isDigit(endIndex + 2) == false) {
                        temp.setCharAt(startIndex - 1, '(');
                        temp.setCharAt(endIndex + 2, ')');
                    }
                }
            }
        }
        input = temp.toString();
        return input;
    }

    private boolean checkForSpecifiedChar(String input, int startIndex) {
        switch (input.charAt(startIndex - 1)) {
            case ' ':
                return true;
            case '(':
                return true;            
            default:
                return false;
        }
    }

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
    protected String deleteRestOfTags(String input) {
        int startIndex = 1;
        int endIndex = 1;

        while (endIndex < input.length()) {
            if (input.charAt(startIndex) != '(') {
                startIndex++;
                endIndex = startIndex;
                endIndex++;
            }   else {
                try {
                    Integer.parseInt(input.substring(startIndex + 1, endIndex + 5));
                }   catch(NumberFormatException e)   {
                    //Finish this
                }
            }
            
        }
    }
}
