package main.java;

import javafx.util.Pair;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


public class ElevatorSystem implements IElevatorSystem {
    private static int numberOfIteration;
    private final TreeMap<Pair<Integer, Integer>, Elevator> freeElevators;
    private final TreeMap<Pair<Integer, Integer>, Elevator> goingUpElevators;
    private final TreeMap<Pair<Integer, Integer>, Elevator> goingDownElevators;
    private final List<Pair<Integer, Integer>> queueWaitingCustomers;

    public List<Pair<Integer, Integer>> getQueueWaitingCustomers() { return queueWaitingCustomers; }


    public ElevatorSystem() {
        numberOfIteration = 0;

        PairComparator pairComparator = new PairComparator();

        freeElevators = new TreeMap<>(pairComparator);
        goingUpElevators = new TreeMap<>(pairComparator);
        goingDownElevators = new TreeMap<>(pairComparator);
        queueWaitingCustomers = new ArrayList<>();
    }

    public void addElevator(Elevator elevator) {
        freeElevators.put(new Pair<>(elevator.getCurrentFloor(), elevator.getID()), elevator);
    }

    private ElevatorState getCorrespondingElevatorState(TreeMap<Pair<Integer, Integer>, Elevator> currentMap) {
        if (currentMap == goingUpElevators)
            return ElevatorState.GOING_UP;
        else if (currentMap == goingDownElevators)
            return ElevatorState.GOING_DOWN;
        else
            return ElevatorState.FREE;
    }

    public void displayStatusOfOneCollection(TreeMap<Pair<Integer, Integer>, Elevator> currentCollection) {
        for (Map.Entry<Pair<Integer, Integer>, Elevator> curr : currentCollection.entrySet()) {
            Elevator currElevator = curr.getValue();
            System.out.print(" [" + currElevator.getID() + ", " + currElevator.getCurrentFloor() + ", " + currElevator.getCurrentDestinationFloor() + "] ");
        }
        System.out.println();
    }

    public void displayStatusOfSystem() {
        int numOfFreeElevators = freeElevators.size();
        int numOfGoingUpElevators = goingUpElevators.size();
        int numOfGoingDownElevators = goingDownElevators.size();

        if (numOfFreeElevators > 0) {
            System.out.println("Free elevators [" + freeElevators.size() + "]:");
            displayStatusOfOneCollection(freeElevators);
        }

        if (numOfGoingUpElevators > 0) {
            System.out.println("Elevators going up [" + goingUpElevators.size() + "]:");
            displayStatusOfOneCollection(goingUpElevators);
        }

        if (numOfGoingDownElevators > 0) {
            System.out.println("Elevators going down [" + goingDownElevators.size() + "]:");
            displayStatusOfOneCollection(goingDownElevators);
        }

        System.out.println();
    }


    private Elevator findBestFreeElevatorToCall(Integer targetFloor) {
        int minDiff = Integer.MAX_VALUE;
        Elevator nearestElevator = null;

        for (Map.Entry<Pair<Integer, Integer>, Elevator> entry : freeElevators.entrySet()) {
            int diff = Math.abs(entry.getKey().getKey() - targetFloor);

            if (diff < minDiff) {
                minDiff = diff;
                nearestElevator = entry.getValue();
            }
        }

        return nearestElevator;
    }

    private Elevator findBestGoingUpElevatorToCall(Integer targetFloor) {
        Pair<Integer, Integer> targetPair = new Pair<>(targetFloor, 0);

        Map.Entry<Pair<Integer, Integer>, Elevator> nearestElevator = goingUpElevators.floorEntry(targetPair);

        if (nearestElevator == null)
            return null;

        return nearestElevator.getValue();
    }

    private Elevator findBestGoingDownElevatorToCall(Integer targetFloor) {
        Pair<Integer, Integer> targetPair = new Pair<>(targetFloor, 0);

        Map.Entry<Pair<Integer, Integer>, Elevator> nearestElevator = goingDownElevators.ceilingEntry(targetPair);

        if (nearestElevator == null)
            return null;

        return nearestElevator.getValue();
    }

    public Pair<TreeMap<Pair<Integer, Integer>, Elevator>, Elevator> findElevatorToPickUp(Integer callFloor, Integer direction) {

        int targetFloor = callFloor;
        Elevator nearestElevator;
        TreeMap<Pair<Integer, Integer>, Elevator> collectionWithNearestElevator;

        if (freeElevators.size() > 0 && goingUpElevators.size() > 0 && direction == 1) {
            Elevator nearestFreeElevator = findBestFreeElevatorToCall(targetFloor);
            Elevator nearestGoingUpElevator = findBestGoingUpElevatorToCall(targetFloor);

            if (nearestGoingUpElevator == null) {
                nearestElevator = nearestFreeElevator;
                collectionWithNearestElevator = freeElevators;
            }
            else if (nearestGoingUpElevator.hasCall(callFloor) ||
                    (callFloor - nearestGoingUpElevator.getCurrentFloor() < Math.abs(callFloor - nearestFreeElevator.getCurrentFloor()) / 2) ||
                    (callFloor - nearestGoingUpElevator.getCurrentFloor() < Math.abs(callFloor - nearestFreeElevator.getCurrentFloor()) && callFloor - nearestGoingUpElevator.getCurrentDestinationFloor() < Math.abs(callFloor - nearestFreeElevator.getCurrentFloor()))
            ) {
                   nearestElevator = nearestGoingUpElevator;
                   collectionWithNearestElevator = goingUpElevators;
            }
            else {
                nearestElevator = nearestFreeElevator;
                collectionWithNearestElevator = freeElevators;
            }
        }
        else if (freeElevators.size() > 0 && goingDownElevators.size() > 0 && direction == -1) {
            Elevator nearestFreeElevator = findBestFreeElevatorToCall(targetFloor);
            Elevator nearestGoingDownElevator = findBestGoingDownElevatorToCall(targetFloor);

            if (nearestGoingDownElevator == null) {
                nearestElevator = nearestFreeElevator;
                collectionWithNearestElevator = freeElevators;
            }
            else if (nearestGoingDownElevator.hasCall(callFloor) ||
                    (nearestGoingDownElevator.getCurrentFloor() - callFloor < Math.abs(nearestFreeElevator.getCurrentFloor() - callFloor) / 2) ||
                    (nearestGoingDownElevator.getCurrentFloor() - callFloor < Math.abs(nearestFreeElevator.getCurrentFloor() - callFloor) && nearestGoingDownElevator.getCurrentDestinationFloor() - callFloor < Math.abs(nearestFreeElevator.getCurrentFloor() - callFloor))
            ) {
                nearestElevator = nearestGoingDownElevator;
                collectionWithNearestElevator = goingDownElevators;
            }
            else {
                nearestElevator = nearestFreeElevator;
                collectionWithNearestElevator = freeElevators;
            }

        }
        else if (freeElevators.size() > 0) {
            nearestElevator = findBestFreeElevatorToCall(targetFloor);
            collectionWithNearestElevator = freeElevators;
        }
        else if (direction == 1 && goingUpElevators.size() > 0) {
            nearestElevator = findBestGoingUpElevatorToCall(targetFloor);
            collectionWithNearestElevator = goingUpElevators;
        }
        else if (direction == -1 && goingDownElevators.size() > 0) {
            nearestElevator = findBestGoingDownElevatorToCall(targetFloor);
            collectionWithNearestElevator = goingDownElevators;
        }
        else
            return null;


        if (nearestElevator == null)
            return null;

        return new Pair<>(collectionWithNearestElevator, nearestElevator);
    }

    public void pickUp(Integer callFloor, Integer direction) {
        Pair<TreeMap<Pair<Integer, Integer>, Elevator>, Elevator> collectionAndElevator = findElevatorToPickUp(callFloor, direction);

        if (collectionAndElevator == null) {
            Pair<Integer, Integer> currentPair = new Pair<>(callFloor, direction);

            queueWaitingCustomers.add(currentPair);

            String directName = (direction == 1) ? "up" : "down";
            System.out.println("No lift available to move from " + callFloor + " floor " + directName + " at the moment, you have been registered in queue, please wait...");
            return;
        }

        Elevator yourElevator = collectionAndElevator.getValue();
        TreeMap<Pair<Integer, Integer>, Elevator> currentCollection = collectionAndElevator.getKey();

        Pair<Integer, Integer> pair = new Pair<>(yourElevator.getCurrentFloor(), yourElevator.getID());

        System.out.println("PickUp registered");
        yourElevator.addCall(callFloor);
        yourElevator.displayStatus();
        System.out.println();


        if (getCorrespondingElevatorState(currentCollection) != yourElevator.getElevatorState()) {
            currentCollection.remove(pair);
            updateDirection(yourElevator);
        }
    }

    public void updateDirection(Elevator elevator) {
        Pair<Integer, Integer> updatedPair = new Pair<>(elevator.getCurrentFloor(), elevator.getID());

        if (elevator.getElevatorState() == ElevatorState.GOING_UP)
            goingUpElevators.put(updatedPair, elevator);
        else if (elevator.getElevatorState() == ElevatorState.GOING_DOWN)
            goingDownElevators.put(updatedPair, elevator);
        else if (elevator.getElevatorState() == ElevatorState.FREE)
            freeElevators.put(updatedPair, elevator);
    }

    private void takeStepForElevator(Elevator elevator) {
        if (elevator.getCurrentFloor() != elevator.getCurrentDestinationFloor()) {

            int makeMove = (elevator.getCurrentFloor() < elevator.getCurrentDestinationFloor()) ? 1 : -1;

            elevator.setCurrentFloor(elevator.getCurrentFloor() + makeMove);
        }
    }

    private void takeStepForCollection(TreeMap<Pair<Integer, Integer>, Elevator> collectionCopy, TreeMap<Pair<Integer, Integer>, Elevator> updatedCollection) {
        updatedCollection.clear();

        for (Map.Entry<Pair<Integer, Integer>, Elevator> current : collectionCopy.entrySet()) {
            Elevator elevator = current.getValue();

            takeStepForElevator(elevator);

            if (elevator.getCurrentFloor() == elevator.getCurrentDestinationFloor()) {

                if (elevator.ifTakePassengerOnCurrentFloor())
                    elevator.clickTargetFloorButton();
                else
                    elevator.releasePassenger();
            }

            updateDirection(elevator);

            elevator.displayStatus();
        }
    }

    public void step() {
        numberOfIteration++;
        System.out.println("\niteration: " + numberOfIteration);

        takeStepForCollection(new TreeMap<>(goingUpElevators), goingUpElevators);

        takeStepForCollection(new TreeMap<>(goingDownElevators), goingDownElevators);

        tryAssignElevatorsToWaitingCustomers();

        System.out.println();
    }

    private void tryAssignElevatorsToWaitingCustomers() {
        List<Pair<Integer, Integer>> oldQueue = new ArrayList<>(queueWaitingCustomers);
        queueWaitingCustomers.clear();

        for (Pair<Integer, Integer> pickUpCall: oldQueue) {
            pickUp(pickUpCall.getKey(), pickUpCall.getValue());
        }
    }
}