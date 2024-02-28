package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface JavaGrep {
    /***
     * Top level search workflow
     * @throws IOException if file open fails
     */
    void process() throws IOException;

    /***
     * Recursively loops through each folder within the rootDir and returns all files
     * @param rootDir input dir
     * @return files under rootDir
     */
    List<File> listFiles(String rootDir);

    /***
     * Read contents of a file, and returns each row in list format
     * @param inputFile file to be read
     * @return each line as a list of Strings
     */
    List<String> readLines(File inputFile);

    /***
     * checks if line matches the regex pattern entered by the user
     * @param line string format of one row within the file
     * @return boolean, true if a match and false otherwise
     */
    boolean containsPattern(String line);

    /***
     * Write the line and the file path for each row from file(s) that match regex
     * @param lines string format of row within the file
     * @throws IOException if the output file fails to open/write
     */
    void writeToFile(List<String> lines) throws IOException;

    /***
     * Getters and Setters
     * Receive and change contents of the 3 user inputs
     * @return the value for each getter
     */
    String getRootPath();
    void setRootPath(String rootPath);
    String getRegex();
    void setRegex(String regex);
    String getOutFile();
    void setOutFile(String outFile);
}
