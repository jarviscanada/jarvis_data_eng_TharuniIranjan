# Introduction
This is a Java application and its purpose is to mimic the Linux grep command. Given a directory, regular expresion, and an output file path, this application will recursively look through each folder within the specified directory. When it comes across a file, it will look through the contents of the file to see if it matches the inputted regex. When the patten matches, it writes that file path to the file the user requested the output in.

# Quick Start
First steps:
```bash
# navigate to the correct directory
cd core_java/grep

# run maven
mvn clean install

# download the jar file
wget -O grep-demo.jar https://github.com/jarviscanada/jarvis_data_eng_demo/raw/feature/grep_demo_jar/core_java/grep/target/grep-1.0-SNAPSHOT.jar
```

To run the application against the test folder and its contents alongside the provided regex, run the following commands:

```bash
# define the 3 cmd line args
regex_pattern = ".*Romeo.*Juliet.*"
src_dir = "./data"
outfile = grep_$(date +%F_%T).txt #change output filename each time it runs
java -jar grep-demo.jar ${regex_pattern} ${src_dir} ./out/${outfile}

# verify results
cat out/$outfile
```

To run against personal example, run the below line:
```bash
java -jar grep-demo.jar ".*Romeo.*Juliet.*" "./src/test/testDir" "./src/test/test_output.txt"
```

# Testing
To test whether the application is running as expected, you can run the below command in your Linux terminal and see if it matches your output file:
```bash
grep -Er ".*Romeo.*Juliet.*" ./data/txt
```
