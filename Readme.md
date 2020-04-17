# Target Disease Analytics

Target Disease Analytics is an analytics engine to process large scale of Target disease relational JSON data. It also provides wrapper over filter REST APIs for querying them and providing the aggregated results..


Note : Both problem’s solutions are in the same project.

## Basic Requirement
JDK 1.8+

SBT

Scala 2.12+

Small Spark cluster locally or on cloud


## Build

Project is built using SBT.
To build Spark and its example programs, run:

    sbt assembly: it should build and generate jar and run the unit tests
    sbt test : to run all test
    HttpTest is might fail sometimes but when ran individually it passes
    It covers all the basic test cases which also includes the problem statements.
    It uses sample inputs with minimal records


    sbt 'set test in assembly := {}' clean assembly
    Build jar w/o tests



## Problem A - query a REST API

### Interactive Shell
Start CLI by running EntryA main method

`java -cp TargetDataAggregator-assembly-1.0.jar com.ds.practice.problema.EntryA`

Inorder to run test cases any cmd mentioned the problem statement can be used
Exit the CLI by typing
`my_code_test exit`

Classes Info

Source code package `com.ds.practice.problema`

`EntryA`  As name suggest used for as a starting point for Problem A cli
It also validates the CLI cmd and submits it to the Queue
When the class is loads it also take cares of initialising,

### Configuration
`src/main/resources/app.properties`
