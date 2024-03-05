# Introduction
This Java application is an implementation of a grep (Global Regular Expression Print) tool, which searches for lines within files that match a specified regular expression pattern. The application accepts three command-line arguments: the regular expression to search for, the root directory path where the search should begin, and the output file path where the matched lines will be written.

# Quick Start
To run the application, follow the below steps:
```bash
# STEP 1: navigate to the correct directory #
cd core_java/grep

# STEP 2: run maven #
mvn clean install

# STEP 3: run application #
outfile=grep_$(date +%F_%T).txt
# Approach 1: JAR File
java -cp target/grep-1.0-SNAPSHOT.jar ca.jrvs.apps.grep.JavaGrepImp ${regex_pattern} ${src_dir} ./out/${outfile}

# Approach 2: Docker Container
docker run --rm \
-v `pwd`/data:/data -v `pwd`/out:/out jrvs/grep \
${regex_pattern} ${src_dir} /out/${outfile}
```
Keep in mind: regex_pattern and src_dir must be defined beforehand, or replaced with strings within the command.

# Implementation 
Here's a brief summary of the key components and functionality of the JavaGrepImp class: <br>

1. It implements the JavaGrep interface, defining methods for setting and getting the regex pattern, root directory path, and output file path.
2. The main method serves as the entry point of the application, where it validates the number of command-line arguments and sets up the JavaGrepImp instance with the provided arguments.
3. The process method performs the top-level search workflow. It traverses through each file within the specified root directory, reads the content of each file, searches for lines matching the regex pattern, and writes the matched lines to the output file.
4. The listFiles method recursively lists all files within a specified directory.
5. The traverse method recursively traverses through directories and adds all file paths to a list.
6. The readLines method reads the contents of a file and returns a list of lines.
7. The containsPattern method checks whether a specified regex pattern is contained within a line.
8. The writeToFile method writes the list of matching lines to the specified output file.

# Test
Logger will indicate whether the application is running without disruptions. In order to determine if the application is running as expected, manual testing can be completed by running the corresponding Linux command within your terminal:
```bash
grep -Er ${regex_pattern} ${src_dir}
```

The below example was used for testing purposes:
```bash
# define the 3 cmd line args
regex_pattern=".*Romeo.*Juliet.*"
src_dir="./data"
# changes output filename each time it runs so you can have a record of results
outfile=grep_$(date +%F_%T).txt 

# set the min and max heap size (optional)
java -Xms5m -Xmx40m -cp target/grep-1.0-SNAPSHOT.jar ca.jrvs.apps.grep.JavaGrepImp ${regex_pattern} ${src_dir} ./out/${outfile}

# view the results
cat out/$outfile
```

# Deployment
Docker was used to dockerize the grep app, and GitHub was used for source code management. When running the application for the first time, make sure to pull the image from DockerHub:
```bash
docker/pull tharunii/grep
```

# Improvement 
1. Add grep options like --ignore-case and --max-count=NUM in the command line argument
2. Make output more visually appealing with rows, columns and titles
3. In the output file, include features: --line-number, --with-filename
4. Include addtional features like the total count of matched lines, and a count for distinct matches

