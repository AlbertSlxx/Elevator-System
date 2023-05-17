# Elevator-System
## General Info
It is an application that allows you to simulate the operation of an elevator system in multi-storey building. The user can create and add any number of elevators that will later handle (in the most efficient way) customers calls.
## System requirements
To be able to run the app, you must have installed a Java Virtual Machine (JVM) version 8 or higher.
## Launching the application
To run the app, compile the source code and run the 'Main' class. The app doesn't require additional call arguments.
## Using the application
Before starting the program, the user can add any number of new elevators to the system (along with information on what floors they are located on) and initial calls for elevators to some floors. 

After launching the application, the user has the option of manually calling the elevators, displaying the current system status (a list of all elevators, along with their current statuses) and performing a single step of the simulation.

During such a step, each of the called elevators will move one floor towards its destination floor. Opening/closing the elevator doors and inputting the target floor by entering customers takes place during the simulation step.

## Algorithm for finding the best elevator for the caller
Calling the elevator in the application is analogous to calling the elevator in the real world - being on a given floor, to call the elevator, press the up or down arrow (depending on which direction you want to go). The pickUp(currentFloor, direction) function is used to call the elevator. After calling the pickUp function, its first step is to call the findElevatorToPickUp function, which tries to find and return the best elevator for the current case. The findElevatorToPickUp function checks the cases in order:

If there are free elevators and there are elevators currently traveling in a direction such as direction, an attempt is made to find two elevators to compare the distance between the floor they are on and the floor of the caller.

- The nearest free elevator == the one with the fewest floors above/below the caller's floor.
- The nearest elevator among those going in a given direction == the one that is the fewest floors above (if the caller clicked down arrow) or below (if the up arrow clicked) the caller's floor.

Now suppose the caller pressed the up arrow.
If both lifts exist (the nearest free and the nearest running), then they are compared. The preferred elevator will be an upward elevator if and only if at least one of the following conditions is true:

- this elevator already has the caller's current floor in the list of its destination floors
- the distance between the caller's floor and the moving elevator is less than half the distance between the caller's floor and the free elevator's floor
- the distance between the calling floor and the moving elevator is less than the distance between the calling floor and the free elevator floor, and the current destination floor of the moving elevator is closer to the calling floor than the current free elevator floor

If none of the conditions are met, or if the sought elevator does not exist (== null), then a free elevator is automatically selected (which will surely exist, because freeElevators.size() > 0).

However, if the condition with two non-empty sets of elevators is not met, then the sets are checked one by one: first the free elevators (if it is non-empty, then one of the elevators belonging to it will go to the caller), then the elevators going down/up (depending on arrows; if the set is non-empty, an attempt is made to find the nearest lift located above/below the caller, in the same way as above; if it fails, null is returned).

FindElevatorToPickUp finally returns a pair of collectionWithNearestElevator and nearestElevator or in the case of a temporary lack of a suitable elevator - null.

After a pair is returned from FindElevatorToPickUp and received in pickUp, it is checked if the pair is null - if so, it means that there is currently no elevator that could handle this request and it is added to the queue, and it will be automatically called again after the simulation step ( step()) which updates the states of all elevators.

If the pair is not null (elevator found), then the elevator and its collection are extracted from the pair. Next, a key pair is created in the tree that holds the collection of elevators (the current floor of the elevator and its ID). Later, for the found elevator, the addCall(callFloor) function is called with the calling floor, which will add it to the list of floors where the elevator must stop. After calling addCall, the current state of the lift may change (e.g. from FREE to GOING_UP), therefore at the end of the pickUp function it is checked whether the state of the lift to which the new stop has been added is the same as the state corresponding to the lifts in the collection in which it is currently located. If not - then using the saved pair (key in the tree) this lift is found and removed from the collection, after which it is added again to the appropriate collection (in the updateDirection() function).

## Author
- Albert Słotwiński