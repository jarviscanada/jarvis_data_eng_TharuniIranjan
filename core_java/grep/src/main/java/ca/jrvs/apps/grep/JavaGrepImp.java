package ca.jrvs.apps.grep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaGrepImp implements JavaGrep {
    private String regex;
    private String rootPath;
    private String outFile;

    final Logger logger = LoggerFactory.getLogger(JavaGrep.class);


    public static void main(String[] args) {
        if (args.length != 3) {
            throw new IllegalArgumentException("Insufficient number of command-line arguments. Expected 3: regex pattern, root directory path, and output file path.");
        }

        // BasicConfigurator.configure();
        JavaGrepImp javaGrepImp = new JavaGrepImp();
        javaGrepImp.setRegex(args[0]);
        javaGrepImp.setRootPath(args[1]);
        javaGrepImp.setOutFile(args[2]);

        try {
            javaGrepImp.process();
        } catch (Exception err) {
            javaGrepImp.logger.error("Error: Unable to process", err);
        }

    }


    @Override
    public String getRootPath() {
        return this.rootPath;
    }

    @Override
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String getRegex() {
        return this.regex;
    }

    @Override
    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public String getOutFile() {
        return this.outFile;
    }

    @Override
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }

    public void process() {
        List<String> matchedLines = new ArrayList<>();
        List<File> listOfFiles = listFiles(getRootPath());

        for (File file : listOfFiles) {
            List<String> fileContent = readLines(file);

            for (String line : fileContent) {
                if (containsPattern(line)) {
                    matchedLines.add(line);
                }
            }
        }

        writeToFile(matchedLines);
    }

    @Override
    public List<File> listFiles(String rootDir) {
        List<File> fileList = new ArrayList<>();
        File newFilePath = new File(rootDir);

        if (newFilePath.exists() && newFilePath.isDirectory()) {
            return traverse(newFilePath, fileList);
        } else {
            return fileList;
        }

    }

    public List<File> traverse(File rootPath, List<File> fileList) {
        // if current item is a file, add to list, else call on method again
        File[] files = rootPath.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    traverse(file, fileList);
                } else {
                    fileList.add(file);
                }
            }
        }
        return fileList;
    }

    @Override
    public List<String> readLines(File inputFile) {
        List<String> lines = new ArrayList<>();
        try {
            String line = "";
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));

            while (line != null) {
                line = reader.readLine();
                lines.add(line);
            }
        } catch (IOException e) {
            this.logger.error("Failed to read file");
        }
        return lines;
    }

    @Override
    public boolean containsPattern(String line) {
        if (line != null) {
            Pattern pattern = Pattern.compile(getRegex());
            Matcher matcher = pattern.matcher(line);

            return matcher.find();
        }
        return false;
    }

    @Override
    public void writeToFile(List<String> lines) {
        try {
            FileWriter fileWrite = new FileWriter(getOutFile(), true);
            for (String line : lines) {
                fileWrite.write(line + "\n");
            }
            fileWrite.close();
        } catch (IOException e) {
            System.out.println("An error occurred when writing to file");
        }
    }
}
