import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Elevator {
    private int ID;
    private int currentFloor;
    private int destFloor;
    private List<Integer> destFloorsList;
    private List<Integer> pickUpFloors;
    private int floorDifference;
    private ElevatorState elevatorState;

    public Elevator (int id, int cf) {
        ID = id;
        currentFloor = cf;
        destFloor = cf;
        destFloorsList = new ArrayList<>();
        pickUpFloors = new ArrayList<>();
        elevatorState = ElevatorState.FREE;
    }

    public int getDestFloor() {
        return destFloor;
    }

    public void setDestFloor(int destFloor) {
        this.destFloor = destFloor;
    }

    public int getFloorDifference() {
        return floorDifference;
    }

    public void setFloorDifference(int floorDifference) {
        this.floorDifference = floorDifference;
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

    public ElevatorState getElevatorState() {
        return elevatorState;
    }

    public void setElevatorState(ElevatorState elevatorState) {
        this.elevatorState = elevatorState;
    }

    public void displayStatus() {
        System.out.println("Lift num " + ID + " is current on " + currentFloor + " floor, destination floor: " + destFloor);
    }

    public void clickTargetFloorButton() {
        Scanner sc = new Scanner(System.in);
        System.out.print(" Lift num " + ID + " is current on " + currentFloor + " floor, doors has been opened, passenger enters the elevator...\n Please enter destination floor: ");

        int passengerDestFloor = sc.nextInt();

        updateDestFloor();
        addDestination(passengerDestFloor);
        //floorDifference = destFloor - currentFloor;
    }

    public void releasePassenger() {
        System.out.println(" Lift num " + ID + " is current on " + currentFloor + " floor, doors has been opened, passenger exits the elevator...");

        updateDestFloor();
    }

    public List<Integer> getDestFloorsList() {
        return destFloorsList;
    }

    public void setDestFloorsList(List<Integer> destFloorsList) {
        this.destFloorsList = destFloorsList;
    }

    public void addCall (int callFloor) {

        // czy to piętro istnieje w kolejce wezwań - jeżeli tak nie dodawaj
        if (!pickUpFloors.contains(callFloor))
            pickUpFloors.add(callFloor);

        addDestination(callFloor);
    }

    public void addDestination (int destinationFloor) {

        // czy to piętro istnieje w kolejce destynacji - jeżeli tak nie dodawaj
        if (!destFloorsList.contains(destinationFloor))
            destFloorsList.add(destinationFloor);

        if (destFloorsList.size() == 1 || destinationFloor < destFloor)
            destFloor = destinationFloor;
    }

    public boolean ifTakePassengerOnCurrentFloor() {
        return pickUpFloors.contains(destFloor);
    }

    public void updateDestFloor() {
        destFloorsList.remove((Object)destFloor);

        if (pickUpFloors.contains(destFloor))
            pickUpFloors.remove((Object)destFloor);

        // różna zmiana w zależności od kierunku jazdy - enum jako pole
        int size = destFloorsList.size();
        if (elevatorState == ElevatorState.GOING_UP && size > 0)
            destFloor = Collections.min(destFloorsList);
        else if (elevatorState == ElevatorState.GOING_DOWN && size > 0)
            destFloor = Collections.max(destFloorsList);
    }

    public int getNumberOfDestinations() {
        return destFloorsList.size();
    }
}
