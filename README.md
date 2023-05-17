# Elevator-System
### General Info
It is an application that allows you to simulate the operation of an elevator system in multi-storey building. The user can create and add any number of elevators that will later handle (in the most efficient way) customers calls.
### System requirements
To be able to run the app, you must have installed a Java Virtual Machine (JVM) version 8 or higher.
### Launching the application
To run the app, compile the source code and run the 'Main' class. The app doesn't require additional call arguments.
### Using the application
Before starting the program, the user can add any number of new elevators to the system (along with information on what floors they are located on) and initial calls for elevators to some floors. 

After launching the application, the user has the option of manually calling the elevators, displaying the current system status (a list of all elevators, along with their current statuses) and performing a single step of the simulation.

During such a step, each of the called elevators will move one floor towards its destination floor. Opening/closing the elevator doors and inputting the target floor by entering customers takes place during the simulation step.