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


    //This mf is dumb
    private boolean checkForSpecifiedChar(String input, int startIndex) {
        switch (input.charAt(startIndex - 1)) {
            case ' ':
                return true;
            case '[':
                return true;
            case ']':
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

        String temp = input;
        StringBuilder output = new StringBuilder(temp);
        char millenium = '1';

        while (startIndex < temp.length() - 3)  {
            //String deleteMe = temp.substring(startIndex);
            

            if (temp.charAt(startIndex + 1) == '9' || temp.charAt(startIndex + 1) == 0) {
                if (Character.isDigit(temp.charAt(startIndex + 2)) && Character.isDigit(temp.charAt(startIndex + 3)))   {

                    if (checkForSpecifiedChar(temp, startIndex) && checkForSpecifiedChar(temp, startIndex + 5)) {
                        output.insert(startIndex, '(');
                        String delMe = output.toString();
                        output.insert(startIndex +5, ')');
                        String delMe2 = output.toString();

                        return output.toString();
                    }
                }
            }

            if (temp.charAt(startIndex) == millenium) {
                startIndex++;
            }
            startIndex = temp.indexOf(millenium, startIndex);
                if (startIndex == -1 && millenium == '1') {
                    temp = input;
                    millenium = '2';
                    startIndex = 1;
                    startIndex = temp.indexOf(millenium, startIndex);
                    if (startIndex == -1) {
                        return output.toString();
                    }
                }   else if (startIndex == -1)  {
                    return output.toString();
                }
        }
        return output.toString();
        //the problem is with counting startIndex
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
