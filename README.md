Payment Tracker
===============
Task
----
Write a program that keeps a record of payments. Each payment includes a currency and an amount.
The program should output a list of all the currency and amounts to the console once per minute.
The input can be typed into the command line, and optionally also be loaded from a file when starting up.

Sample input:  
USD 1000  
HKD 100  
USD -100  
RMB 2000  
HKD 200  

Sample output:  
USD 900  
RMB 2000  
HKD 300  

Detailed requirements
When your Java program is run, a filename can be optionally specified.
The format of the file will be one or more lines with Currency Code Amount like in the Sample Input above,
where the currency may be any uppercase 3 letter code, such as USD, HKD, RMB, NZD, GBP etc.
The user can then enter more lines into the console by typing a currency and amount and pressing enter.
Once per minute, the output showing the net amounts of each currency should be displayed.
If the net amount is 0, that currency should not be displayed.  When the user types "quit", the program should exit.

You may need to make some assumptions. For example, if the user enters invalid input,
you can choose to display an error message or quit the program.
For each assumption you make, write it down in a readme.txt and include it when you submit the project.

Things you may consider using:
Unit testing
Threadsafe code
Programming patterns

Please put your code in a bitbucket/github repository.
We should be able to build and run your program easily (you may wish to use Maven, Ant, etc).
Include instructions on how to run your program.

Optional bonus question
Allow each currency to have the exchange rate compared to USD configured. When you display the output,
write the USD equivalent amount next to it, for example:  
USD 900  
RMB 2000 (USD 314.60)  
HKD 300 (USD 38.62)  

How to install and run
----------------------
1. 'git clone https://github.com/dominikjenik/Payment-Tracker.git' or download manually
2. 'cd Payment-Tracker' directory
3. 'mvn package'
4. 'cd target' folder
5. 'java -jar payment_tracker-1.0-jar-with-dependencies.jar [input filename]' 

Assumptions for running the program
-----------------------------------
java 8  
maven 3.x  

Assumptions
-----------
- User can write exchange rate only absolute value without + or - mark. 
For transaction is only important the sign on amount.
e. g. HKD -300 (USD -38.62) is invalid, use instead HKD -300 (USD 38.62)
- max amount is 10^32 with 4 decimal places
- for see examples of valid and invalid messages type "help"
- for debug configuration use VM option -Dconfig.resource=debug.conf

elaboration
-----------
24.9.15 created project, github dir, added default akka config

25.9.15 thread for console output received from inbox, sending messages from user to actors.RouterActor

26.9.15 added debug logging, added quit msg, created input pattern

27.9.15 from thread for consoled output made actor, FileHandler actor for reading file, 
TransactionCounter for holding info about transactions, added testing with TESTNG, 
added test for pattern, pattern modified, added Message about not match pattern and for hold payment data,
code-ref pattern unit-test, added group to pattern, tested matcher groups,
appending to file newest messages, added transaction logic with tests, pattern changes, added help command, 
adding scheduler for regular output, creating list of payment messages for output
adding test for output payment messages, 
correcting messages and actors ownership, fixed zero when creating payment track, replacing junit by testng
solving problem with duplicate loading files, routing based on interfaces, reading different that default file

28.9.15 default configuration without debugging config, building project into jar,
added Factory design pattern, adding PaymentMessageMatcher to avoid multiple calling .group method

29.9.15 bug fixes-addBigValuesOverLimit, addMaxValueToExchangeAmountAndIncreaseAmount