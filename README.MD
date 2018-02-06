## CS361 Foundations of Security. Assignment 2 (Password Cracking) [![MIT license](https://img.shields.io/badge/license-MIT-lightgrey.svg)](https://https://raw.githubusercontent.com/qirh/CS361-assignment2/master/LICENSE)

### Description
Java project by me and [@Ovais](https://github.com/theBrovais). Done for CS f361, taught by the amazing Dr. Bill Young in the summer of 2016. For assignment details, please look [here](https://github.com/qirh/CS361-assignment2/blob/master/assignment2.pdf)

There is only one java file named *CovertChannel.java*, this file has both the run harness and the rest of the code. A BLP Secure System was implemented in this project, building off the last project. With the addition of a Covert channel this time. The covert channel functionality is implemented in the methods *execute()* and it's helper method, conveniently named *executeHelper()*, both residing in the CovertChannel class.

To compile the program
```
javac CoverChannel.java
```
The program will not run however without an input file. The program will also take an optional flag 'v' for verbose to be supplied before the input file. If supplied, the program will output all the covert channel commands to a file named log in the same directory as the code.

With the parameter of an input file, to run, use
```
java CoverChannel xxx
```
with xxx being the input file. Both Ovais and Saleh worked together on this project, we used peer programming and worked on the same screen for the whole time. From a top level view, the code reads the input file, the parses each line into bytes and Covert Channel transmits bits from high subjects to low, which are then written into the output file.

### Compile Machine Information
Windows 10, MSI GT80s, Core i7-6820HK, 3.7 GHZ

### Source Description
The first two test files are novels the were acquired from the internet. They are 'Pride and Prejudice' and 'Metamorphosis' respectively. The third one which is called Test3 is a paragraph from the project requirement document, the fourth one is an excerpt from the chronicle about a collective in non-profit East Austin.

### Finish
We met all the requirements. There might be a bug (based on the system you're running this on) for file inputs. We think we fixed it, but if there is a FileNotFoundException or a StringIndexOutOfBoundsException. That means the program messed up and put an extra slash, please provide the input file by providing the full path to it, not just the file's name

### Results Summary
| No.| DocumentName        | Size      | Bandwidth   |
|----|---------------------|-----------|-------------|
|  1 | Pride and Prejudice | 684,767 B | 396.40 b/ms |
|  2 | Metamorphosis       | 118,623 B | 307.62 b/ms |
|  3 | Test3               | 322 B     | 87.34 b/ms  |
|  4 | Test4               | 233 B     | 53.27 b/ms  |
