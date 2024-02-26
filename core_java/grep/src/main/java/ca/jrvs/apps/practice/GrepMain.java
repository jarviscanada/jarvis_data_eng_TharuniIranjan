package ca.jrvs.apps.grep;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GrepMain {
    /***
     * Main accepts 3 arguments and calls on the appropriate methods to recursively looks through a directory
     * and checks to see what files match a specific regex patten. Matching file paths are written to an output file.
     * @param args accepts 3 command line arguments inputted by the user, which is passed and understood us a regular expression, root directory, and file path to an output file
     * @throws IOException will throw an error if the root directory doesn't exist or could not be found
     */
    public static void main(String[] args) throws IOException {
        if (args.length != 3) {
            throw new IllegalArgumentException("Insufficient number of command-line arguments. Expected 3: regex pattern, root directory path, and output file path.");
        } else {
            String regex = args[0];
            String rootDir = args[1];
            String outputFilePath = args[2];


            File newFilePath = new File(rootDir);
            if (newFilePath.isDirectory()) {
                openDir(newFilePath, regex, outputFilePath);
            } else {
                throw new FileNotFoundException("Root directory path does not exist");
            }
        }
    }

    /***
     * Opens the specified file and recursively looks through each item
     * @param root folder path for the specified root directory
     * @param regex regular expression patten
     * @param output file path for the output file
     * @throws IOException will throw an error if it is unable to loop through the directory content
     */
    public static void openDir(File root, String regex, String output) throws IOException {
        // loop through each folder within the root directory
        File[] files = root.listFiles();
        if (files != null) {
            for (File file : files) {
                // if current item is a folder, call on function again to look through its contents
                if (file.isDirectory()) {
                    openDir(file, regex, output);
                } else {
                    // if current item is a file, call on the checkRegex method
                    checkRegex(file, regex, output);
                }
            }
        }
    }

    /***
     * Writes a file path to the specified output file
     * @param filePath file path of the file that matches the regex
     * @param outputFilePath file path of the output file that will be written into
     */
    public static void writeToFile(File filePath, String outputFilePath) {
        try {
            // open a FileWriter to the outputFilePath, and append the filePath variable to the end of the file with a new line
            FileWriter fileWrite = new FileWriter(outputFilePath, true);
            fileWrite.write(filePath.toString() + "\n");
            fileWrite.close();
        } catch (IOException e) {
           System.out.println("An error occurred when writing to file");
        }
    }

    /***
     * Open the contents of a file, and check if it matches a specified regex
     * @param filePath path to the file we want to read
     * @param regex regex we want to check against the file
     * @param output the path to the file we want to write to
     * @throws IOException will throw an error if the file does not exist or fails to read
     */
    public static void checkRegex(File filePath, String regex, String output) throws IOException{
        // extract file content into a list and convert that into a string for regex
        List<String> contentList = Files.readAllLines(filePath.toPath());
        String contentString = String.join(" ", contentList);

        // define the regex pattern as what the user inputted and where to search as the file contents
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(contentString);
        boolean matchFound = matcher.find();

        if (matchFound) {
            writeToFile(filePath, output);
        }
    }
}
