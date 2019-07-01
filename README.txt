The live order board project can be compiled using Maven :

mvn clean install

This will run the unit tests and create the jar file : 

lob-1.0-SNAPSHOT.jar

While creating the code there was a design decision I had to make.
Currently the code saves every order and creates the summary on the fly.
I could have refactored the code to only store the summary which may be a more optimised solution.
Retrieving the summary would be quicker but the registering and cancelling of orders would be more complicated.
Also, individual order information would be lost and while the board does not require the user information this
could be important in the future.