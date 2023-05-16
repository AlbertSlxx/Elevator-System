import javafx.util.Pair;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


public class ElevatorSystem implements IElevatorSystem {
    private static int numberOfIteration;
    private final List<Triple<Integer, Integer, Integer>> elevatorList;
    private final TreeMap<Pair<Integer, Integer>, Elevator> freeElevators;
    private final TreeMap<Pair<Integer, Integer>, Elevator> goingUpElevators;
    private final TreeMap<Pair<Integer, Integer>, Elevator> goingDownElevators;
    private final List<Pair<Integer, Integer>> queueWaitingCustomers;


    public ElevatorSystem() {
        numberOfIteration = 0;

        elevatorList = new ArrayList<>();

        PairComparator pairComparator = new PairComparator();

        freeElevators = new TreeMap<>(pairComparator);
        goingUpElevators = new TreeMap<>(pairComparator);
        goingDownElevators = new TreeMap<>(pairComparator);
        queueWaitingCustomers = new ArrayList<>();
    }

    public void addElevator(Elevator elevator) {
        freeElevators.put(new Pair<>(elevator.getCurrentFloor(), elevator.getID()), elevator);
    }

    public Pair<TreeMap<Pair<Integer, Integer>, Elevator>, Elevator> findElevatorToPickUp(Integer callFloor, Integer direction) {

        int targetFloor = callFloor;

        if (freeElevators.size() > 0) {

            int minDiff = Integer.MAX_VALUE;
            Elevator nearestElevator = null;

            for (Map.Entry<Pair<Integer, Integer>, Elevator> entry : freeElevators.entrySet()) {
                int diff = Math.abs(entry.getKey().getKey() - targetFloor);

                if (diff < minDiff) {
                    minDiff = diff;
                    nearestElevator = entry.getValue();
                }
            }

            if (nearestElevator == null)
                return null;

            return new Pair<>(freeElevators, nearestElevator);
        }
        else if (direction == 1 && goingUpElevators.size() > 0) {

            Pair<Integer, Integer> targetPair = new Pair<>(targetFloor, 0);

            Map.Entry<Pair<Integer, Integer>, Elevator> nearestElevator = goingUpElevators.floorEntry(targetPair);

            if (nearestElevator == null)
                return null;

            return new Pair<>(goingUpElevators, nearestElevator.getValue());
        }
        else if (direction == -1 && goingDownElevators.size() > 0) {

            Pair<Integer, Integer> targetPair = new Pair<>(targetFloor, 0);

            Map.Entry<Pair<Integer, Integer>, Elevator> nearestElevator = goingDownElevators.ceilingEntry(targetPair);

            if (nearestElevator == null)
                return null;

            return new Pair<>(goingDownElevators, nearestElevator.getValue());
        }
        else
            return null;

    }

    public void pickup(Integer callFloor, Integer direction) {
        Pair<TreeMap<Pair<Integer, Integer>, Elevator>, Elevator> collectionAndElevator = findElevatorToPickUp(callFloor, direction);

        if (collectionAndElevator == null) {
            Pair<Integer, Integer> currentPair = new Pair<>(callFloor, direction);

            queueWaitingCustomers.add(currentPair);

            String directName = (direction == 1) ? "up" : "down";
            System.out.println("No lift available to move from " + callFloor + " floor " + directName + " at the moment, please wait...");
            return;
        }

        Elevator yourElevator = collectionAndElevator.getValue();
        TreeMap<Pair<Integer, Integer>, Elevator> currentCollection = collectionAndElevator.getKey();

        Pair<Integer, Integer> pair = new Pair<>(yourElevator.getCurrentFloor(), yourElevator.getID());

        System.out.println("PickUp registered");
        yourElevator.addCall(callFloor);
        yourElevator.displayStatus();
        System.out.println();

        if (currentCollection == freeElevators) {
            freeElevators.remove(pair);

            if (direction == 1) {
                yourElevator.setElevatorState(ElevatorState.GOING_UP);
                goingUpElevators.put(pair, yourElevator);
            }
            else {
                yourElevator.setElevatorState(ElevatorState.GOING_DOWN);
                goingDownElevators.put(pair, yourElevator);
            }
        }
    }

    public void update(TreeMap<Pair<Integer, Integer>, Elevator> currentMap, Elevator elevator) {
        if (elevator.getCurrentFloor() != elevator.getCurrentDestinationFloor()) {

            int makeMove = (elevator.getCurrentFloor() < elevator.getCurrentDestinationFloor()) ? 1 : -1;

            elevator.setCurrentFloor(elevator.getCurrentFloor() + makeMove);
        }

        Pair<Integer, Integer> updatedPair = new Pair<>(elevator.getCurrentFloor(), elevator.getID());
        currentMap.put(updatedPair, elevator);
    }

    public void step() {
        numberOfIteration++;
        System.out.println("\niteration: " + numberOfIteration);

        takeStepForCollection(new TreeMap<>(goingUpElevators), goingUpElevators);

        takeStepForCollection(new TreeMap<>(goingDownElevators), goingDownElevators);

        tryAssignElevatorsToWaitingCustomers();

        System.out.println();
    }

    private void takeStepForCollection(TreeMap<Pair<Integer, Integer>, Elevator> collectionCopy, TreeMap<Pair<Integer, Integer>, Elevator> updatedCollection) {
        updatedCollection.clear();

        for (Map.Entry<Pair<Integer, Integer>, Elevator> current : collectionCopy.entrySet()) {
            Elevator elevator = current.getValue();

            update(updatedCollection, elevator);

            if (elevator.getCurrentFloor() == elevator.getCurrentDestinationFloor()) {

                if (elevator.ifTakePassengerOnCurrentFloor())
                    elevator.clickTargetFloorButton();
                else {
                    elevator.releasePassenger();

                    if (elevator.getNumberOfDestinations() == 0) {

                        elevator.setElevatorState(ElevatorState.FREE);
                        Pair<Integer, Integer> updatedPair = new Pair<>(elevator.getCurrentFloor(), elevator.getID());

                        updatedCollection.remove(updatedPair);
                        freeElevators.put(updatedPair, elevator);
                    }
                }
            }

            elevator.displayStatus();
        }
    }

    private void tryAssignElevatorsToWaitingCustomers() {
        List<Pair<Integer, Integer>> oldQueue = new ArrayList<>(queueWaitingCustomers);
        queueWaitingCustomers.clear();

        for (Pair<Integer, Integer> pickUpCall: oldQueue) {
            pickup(pickUpCall.getKey(), pickUpCall.getValue());
        }
    }

    public List<Triple<Integer, Integer, Integer>> status() {
        return elevatorList;
    }
}
