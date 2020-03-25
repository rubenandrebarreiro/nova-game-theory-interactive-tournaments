This is a short manual to this program that covers three fundamental aspects.
1. Importing the project into Eclipse.
2. Running the application from the command line.
3. Adding a new Strategy.

Note that this program resorts to a Jar with multiple pre-compiled classes
that materialise the engine of the client. These classes were compiled using
Java version 1.8, and hence you must run the client using at least java 1.8.
If you are unsure about the java version installed in your machine run the
command "java -version" in the command line and verify that the output refers
java 1.8. When compiling your code in Eclipse, you should also use java 1.8
and hence the Java  JDK 1.8 is required.

1. Importing the project into Eclipse.

Go into your Eclipse workspace and unzip the file containing this project.
The unzip procedure should create the directory 'gt-game' where this 
README file can be found. Afterwards, go to Eclipse and create a new
Java project (using the appropriate option in the 'File' menu) and give 
the name 'gt-game' to the new project. Eclipse should realise that the project
already exists on disk. Press finish and the project will be fully imported
into Eclipse.

2. Running the application from the command line.
The application should be run with the java command, providing as class 
path the bin directory and the jar within the lib directory. Execution
of the program must be performed inside the gt-game directory (where this
README is located). The class that has the main is called Main (and is
contained within the companion jar within the lib directory). Companion
scripts for OsX/Linux and Windows available in the gt-game directory can
be used. These scripts can be run by issuing the command: ./runClient in
OsX/Linux and runClient.bat in windows.


3. Adding a new Strategy.
Create a class that extends the Strategy abstract class anywhere within
the project. Follow the examples of the two strategies provided
with this client that are contained within the play package. Essentially,
each strategy should implement the method execute that should (at some
point) have a (infinite) cycle. This cycle should start by receiving
a PlayStrategy instance (which indicates that a play should be made),
fill out this strategy and, before finishing, deliver the
PlayStrategy back to the client engine (follow the examples).
The methods that are available in the PlayStrategy are documented
in the PlayStrategyInterface.
After creating this class, to make it available as an alternative
for the game engine when playing games, you must add a line to the file
Strategy.conf (that can be found in the root of the gt-game directory).
This line should have the full name of your class. As an example, if
your class is named SmartStrategy, and is located within a package named
smart, that itself is within a package named mystrategies, you should 
add the following line to the Strategy.conf:
mystrategies.smart.SmartStrategy
These should be no empty line in the end of the Strategy.conf file.


Please email any comments and suggestions to your lecturer.
We hope you like to use this pedagogical tool.


