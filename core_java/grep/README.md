# Introduction
This is a Java application and its purpose is to mimic the Linux grep command. Given a directory, regular expresion, and an output file path, this application will recursively look through each folder within the specified directory. When it comes across a file, it will look through the contents of the file to see if it matches the inputted regex. When the patten matches, it writes that file path to the file the user requested the output in.

# Quick Start
To run the application against the test folder and it the test regex, open the grep folder and run the command:
java main/java/ca/jrvs/apps/practice/GrepMain.java ".*Romeo.*Juliet.*" "test/testDir" "test/test_output.txt"

# Testing
To test whether the application is running as expected, you can run the below command in your Linux terminal and see if it matches your output file:
grep -Er '.*Romeo.*Juliet.*' test/testDir
