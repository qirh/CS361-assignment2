UTEID: mp34495; soa322;
FIRSTNAME: Ovais; Saleh;
LASTNAME: Panjwani; Alghusson;
CSACCOUNT: mp34495; almto3;
EMAIL: ovais.panjwani@utexas.edu; almto3@hotmail.com;

[Program 2]
[Description]
There is only one java file named CovertChannel.java, this file has both the run harness and the rest of the code. A BLP Secure System was implemented in this project, building off the last project. With the addition of a Covert channel this time. The covert channel functionality is implemented in the methods execute() and it's helper method, conveniently named executeHelper(), both residing in the CovertChannel class. To compile the program, you need to use "javac CoverChannel.java". The program will not run however without an input file. The program will also take an optional flag 'v' for verbose to be supplied before the input file. If supplied, the program will output all the covert channel commands to a file named log in the same directory as the code. With the parameter of an input file,  To run our program, you need to use "java CoverChannel xxx" with xxx being the input file. Both Ovais and Saleh worked together on this project, we used peer programming and worked on the same screen for the whole time. From a top level view, the code reads the input file, the parses each line into bytes and Covert Channel transmits bits from high subjects to low, which are then written into the output file.

[Machine Information]
Windows 10, MSI GT80s, Core i7-6820HK, 3.7 GHZ

[Source Description]
The first two test files are novels the were acquired from the internet. Res are 'Pride and Prejudice' and 'Metamorphosis' respectively. The third one which is called Test3 is a paragraph from the project requirement document, the fourth one is an excerpt from the chronicle about a collective in non-profit East Austin.

[Finish]
We finished all Requirements, with no known bugs

[Results Summary]
[No.]	[DocumentName] 		[Size] 	 	[Bandwidth]
1	Pride and Prejudice	684,767 bytes	396.40 bits/ms
2	Metamorphosis		118,623 bytes	307.62 bits/ms
3	Test3				322 bytes		87.34 bits/ms
4	Test4				233 bytes		53.27 bits/ms