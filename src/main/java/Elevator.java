package main.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Elevator {
    private int ID;
    private int currentFloor;
    private int currentDestinationFloor;
    private ElevatorState elevatorState;
    private List<Integer> floorsToStop;
    private List<Integer> floorsToPickUp;


    public Elevator(int id) {
        ID = id;
        currentFloor = 0;
        currentDestinationFloor = 0;
        elevatorState = ElevatorState.FREE;
        floorsToStop = new ArrayList<>();
        floorsToPickUp = new ArrayList<>();
    }

    public Elevator (int id, int floor) {
        ID = id;
        currentFloor = floor;
        currentDestinationFloor = floor;
        elevatorState = ElevatorState.FREE;
        floorsToStop = new ArrayList<>();
        floorsToPickUp = new ArrayList<>();
    }


    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }
    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public int getCurrentDestinationFloor() {
        return currentDestinationFloor;
    }
    public void setCurrentDestinationFloor(int currentDestinationFloor) {
        this.currentDestinationFloor = currentDestinationFloor;

        if (currentDestinationFloor > currentFloor)
            setElevatorState(ElevatorState.GOING_UP);
        else if (currentDestinationFloor < currentFloor)
            setElevatorState(ElevatorState.GOING_DOWN);
    }

    public ElevatorState getElevatorState() { return elevatorState; }
    public void setElevatorState(ElevatorState elevatorState) { this.elevatorState = elevatorState; }

    public List<Integer> getFloorsToStop() {
        return floorsToStop;
    }
    public void setFloorsToStop(List<Integer> floorsToStop) {
        this.floorsToStop = floorsToStop;
    }

    public List<Integer> getFloorsToPickUp() { return floorsToPickUp; }
    public void setFloorsToPickUp(List<Integer> floorsToPickUp) { this.floorsToPickUp = floorsToPickUp; }

    public int getNumberOfDestinations() {
        return floorsToStop.size();
    }

    public boolean ifTakePassengerOnCurrentFloor() { return floorsToPickUp.contains(currentDestinationFloor); }

    public boolean hasCall(int floor) { return floorsToPickUp.contains(floor); }


    public void displayStatus() {
        System.out.println("Lift num " + ID + " is current on " + currentFloor + " floor, destination floor: " + currentDestinationFloor + ". Status = " + elevatorState);
    }

    public void clickTargetFloorButton() {
        Scanner sc = new Scanner(System.in);
        System.out.print(" [Lift num: " + ID + "] Doors has been opened, passenger enters the elevator...\n [Lift num: " + ID + "] Please enter destination floor: ");

        int passengerDestFloor = sc.nextInt();

        updateDestFloor();
        addDestination(passengerDestFloor);
    }

    public void releasePassenger() {
        System.out.println(" [Lift num: " + ID + "] Doors has been opened, passenger exits the elevator...");

        updateDestFloor();
    }


    public void addCall (int callFloor) {

        if (!floorsToPickUp.contains(callFloor))
            floorsToPickUp.add(callFloor);

        addDestination(callFloor);
    }

    public void addDestination (int newDestinationFloor) {

        if (!floorsToStop.contains(newDestinationFloor))
            floorsToStop.add(newDestinationFloor);

        if (
                (newDestinationFloor < currentDestinationFloor && elevatorState == ElevatorState.GOING_UP) ||
                (newDestinationFloor > currentDestinationFloor && elevatorState == ElevatorState.GOING_DOWN) ||
                (floorsToStop.size() == 1)
           )
        {
            setCurrentDestinationFloor(newDestinationFloor);
        }
    }

    public void updateDestFloor() {

        floorsToStop.remove((Object) currentDestinationFloor);

        if (floorsToPickUp.contains(currentDestinationFloor))
            floorsToPickUp.remove((Object) currentDestinationFloor);

        int size = floorsToStop.size();
        if (size > 0 && elevatorState == ElevatorState.GOING_UP)
            setCurrentDestinationFloor(Collections.min(floorsToStop));
        else if (size > 0 && elevatorState == ElevatorState.GOING_DOWN)
            setCurrentDestinationFloor(Collections.max(floorsToStop));
        else if (size == 0)
            setElevatorState(ElevatorState.FREE);
    }
}
