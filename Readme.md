Target Disease Analytics
Target Disease Analytics is an analytics engine to process large scale of Target disease relational JSON data. It also provides wrapper over filter REST APIs for querying them and providing the aggregated results.

Note : Both problem’s solutions are in the same project.
Basic Requirement
JDK 1.8+
SBT
Scala 2.12+
Small Spark cluster locally or on cloud
Building source code
sbt assembly: it should build and generate jar and run the unit tests
sbt test : to run all test
HttpTest is might fail sometimes but when ran individually it passes
It covers all the basic test cases which also includes the problem statements.
It uses sample inputs with minimal records


sbt 'set test in assembly := {}' clean assembly
Build jar w/o tests

Problem A - query a REST API
Interactive Shell
Start CLI by running EntryA main method
java -cp TargetDataAggregator-assembly-1.0.jar com.ds.practice.problema.EntryA
Inorder to run test cases any cmd mentioned the problem statement can be used
Exit the CLI by typing
my_code_test exit
Classes Info
Source code package com.ds.practice.problema
EntryA 
As name suggest used for as a starting point for Problem A cli
It also validates the CLI cmd and submits it to the Queue
When the class is loads it also take cares of initialising,
Request Queue
Scores Queue
Thread pool for Consuming Request Queue
Thread pool for Consuming Scores Queue
Whenever the cli terminate cmd is execute it also terminates the thread pools with it
Consumer
RequestConsumer : Consumer from request queue and pushes scores to Scores queue
ScoreAggConsumer : Consumes from Score queue and prints output
Rest of classes are helper classes which have code distributed among them according to the responsibilities.
Configuration
Properties file
location - src/main/resources/app.properties
By default the above props file is picked up by application
app.filter.url : Application REST url for filter API
app.cores : total no. of CPUs on he machine
Log4j properties
To configure slf4j appender , message format, etc

Problem B - parse a JSON dump
Framework used
Spark core for reading processing and saving the JSON dump
Spark Data frame is underlying data structure used for data processing
Common Classes Info
Calculator : Calculates average and median 
EntryB : It triggers Spark job for First or Second part solutions.
VM Args 
-Dspark.master=local[*]
Program Args :
Input file path
Should a JSON file (gz or plain)
Output file path
CSV files
Solution
first/second

To run First Part/ Second Part solution you will require Spark cluster and then below cmd can be submitted to it.
{spark-base-dir}/bin/spark-submit  \
--class com.ds.practice.problemb.EntryB \
--master spark://localhost:7077 \
${Jar-path}/TargetDataAggregator-assembly-1.0.jar \
${input-file-path}/17.12_17.12_evidence_data.json.gz \ 
{output-file-path} {first/second}

OR
It can be via any IDE like Intellij

First part
Classes Info
Source code package com.ds.practice.problemb
FirstPartSolution : Solution for Second part
TargetDiseaseDataProcessor : All the data frame handling code goes here


Second part
Classes Info
Source code package com.ds.practice.problemb
SecondPartSolution : Solution for Second part 
VM Args 
-Dspark.master=local[*] or any other Spark master component
Program Args :
Input file path
Should a JSON file (gz or plain)
Output file path
CSV files
