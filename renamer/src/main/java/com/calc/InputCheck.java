package com.calc;

public class InputCheck {

    protected String runAllParsers(String input)    {
        String fileExtension = getFileExtension(input);
        if (fileExtension.length() < input.length()) {
            input = removeFileExtension(input);
        }   else    {
            fileExtension = "";
        }
        input = changeDotsToSpaces(input);
        input = deleteResolutionTag(input);
        input = parenthesisTheYear(input);
        input = removeNonimdbTags(input);
        input = deleteRestOfTags(input);
        input = deleteLastSpace(input);

        return input + fileExtension;
    }
    
    //TODO error handling for unknown file extension
    private String getFileExtension(String input)   {
        String output = input;

        if (input.lastIndexOf('.') == -1) {
            return output;
        }
        String fileExtension = input.substring(input.lastIndexOf('.'));

        //This needs to be expanded if necessary
        switch (fileExtension) {
            case ".mp4":
                return fileExtension;
            case ".srt":
                return fileExtension;
            case ".nfo":
                return fileExtension;
            case ".jpg":
                return fileExtension;
            case ".png":
                return fileExtension;
            case ".mkv":
                return fileExtension;
            case ".txt":
                return fileExtension;
            case ".avi":
                return fileExtension;
            case ".idx":
                return fileExtension;
            case ".sub":
                return fileExtension;
            case ".flv":
                return fileExtension;
            case ".exe":
                return fileExtension;
            case ".webm":
                return fileExtension;
            default:
                fileExtension = "";
                return fileExtension;

        }
    }

    private String removeFileExtension(String input)    {
        String output = input.substring(0, input.lastIndexOf('.'));
        return output;
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
    private String changeDotsToSpaces(String input)   {
        String output = input;
        output = output.replace('.', ' ');
        return output;
    }

    private String deleteLastSpace(String input)    {
        String output = input;
        if (output.indexOf(output.length()+1) == ' ') {
            output = output.substring(0, output.length() + 1);
        }
        return output;
    }
    

    //TODO: test
    protected String removeNonimdbTags(String input)  {
        int startIndex = 1;
        int endIndex = 2;
        StringBuilder output = new StringBuilder(input);        
            
        //This code is not efficient but should work for now
        while (output.indexOf("[") != -1) {
           // startIndex = output.indexOf("[");
            //checks for imdb id tag "[tt"
            if (output.substring(startIndex, startIndex + 3).equals("[tt") == true) {
                startIndex = output.indexOf("]");
                //If tag exists checks if there are other brackets and deletes them from the string builder
                if (output.substring(startIndex).indexOf("[") != -1) {
                    startIndex = output.substring(startIndex).indexOf("[");
                    endIndex = output.substring(startIndex).indexOf("]");
                    output.delete(startIndex, endIndex + 1);
            }
            }   else    {
                startIndex = output.indexOf("[");
                endIndex = output.indexOf("]");
                output.delete(startIndex, endIndex + 1);
            }    
        }
        return output.toString();
    }

    //TODO: FIX THIS
    private String parenthesisTheYear(String input)   {
        int startIndex = 1;
        int endIndex = 3;

        String temp = input;

        startIndex = temp.substring(startIndex).indexOf('1');
            if (startIndex == -1) {
                startIndex = temp.substring(startIndex).indexOf('2');
            }   else if (startIndex == -1)  {
                return temp;
            }

        if (temp.indexOf('1') != -1 && temp.charAt(temp.indexOf(1) + 1) == '9') {
            if (Character.isDigit(temp.charAt(startIndex + 3)) && Character.isDigit(temp.charAt(startIndex + 4)))   {
                if (checkForSpecifiedChar(temp, startIndex)) {
                    
                }
            }
        }
        //This is just a mess
        /*
        while (endIndex < input.length() - 2) {
            if (input.substring(startIndex, endIndex) == "19" || input.substring(startIndex, endIndex) == "20") {
                if (checkForSpecifiedChar(input, startIndex) == true) {
                    if (Character.isDigit(input.charAt(endIndex)) == true && Character.isDigit(input.charAt(endIndex + 1)) == true) {
                        if (Character.isDigit(endIndex + 2) == false) {
                            output.setCharAt(startIndex - 1, '(');
                            output.setCharAt(endIndex + 2, ')');
                        }
                    }
                }
            }
            startIndex++;
            endIndex++;
        }
                    return output.toString();

        */

    }



    //TODO: test
    private String deleteResolutionTag(String input)   {
        String resolution = "420p";
        boolean state = true;

        while (state == true)   {
            if (input.contains(resolution)) {
                String temp = input;
                //This might cause problems
                temp = temp.substring(input.indexOf(resolution) + resolution.length());
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

    //TODO: test 
    private String deleteRestOfTags(String input) {
        String output = input;
        int startIndex = 1;
        int endIndex = 1;
        int year = 0;

        //This could be done faster with a smarter algorithm
        while (endIndex < output.length()) {
            if (output.charAt(startIndex) != '(') {
            }   else {
                try {
                    year = Integer.parseInt(output.substring(startIndex + 1, endIndex + 5));
                }   catch(NumberFormatException e)   {
                }
            }
            startIndex++;
            endIndex = startIndex;
            endIndex++;
            if (year != 0) {
                break;
            }
        }

        endIndex = output.indexOf(')');
          if (endIndex == -1) {
            return output;
        }
        output = output.substring(0, endIndex + 1);
        return output;
    }
}
