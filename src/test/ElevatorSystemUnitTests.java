import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.java.*;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;


public class ElevatorSystemUnitTests {
    private ElevatorSystem elevatorSystem;

    @BeforeEach
    public void setup() {
        elevatorSystem = new ElevatorSystem();
    }


    @Test
    public void testPickupWhenFreeElevatorAvailable() {
        int callFloor = 3;
        Integer direction = 1;

        Elevator elevator = new Elevator(1);
        elevatorSystem.addElevator(elevator);

        elevatorSystem.pickUp(callFloor, direction);


        Assertions.assertTrue(elevator.hasCall(callFloor));
    }

    @Test
    public void testPickupWhenGoingUpElevatorAvailable() {
        int callFloor = 3;
        Integer direction = 1;

        Elevator elevator = new Elevator(1);
        elevator.setCurrentFloor(2);
        elevator.setElevatorState(ElevatorState.GOING_UP);
        elevatorSystem.addElevator(elevator);

        elevatorSystem.pickUp(callFloor, direction);


        Assertions.assertTrue(elevator.hasCall(callFloor));
    }

    @Test
    public void testPickupWhenGoingDownElevatorAvailable() {
        int callFloor = 3;
        Integer direction = 1;

        Elevator elevator = new Elevator(1);
        elevator.setCurrentFloor(7);
        elevator.setElevatorState(ElevatorState.GOING_DOWN);
        elevatorSystem.addElevator(elevator);

        elevatorSystem.pickUp(callFloor, direction);


        Assertions.assertTrue(elevator.hasCall(callFloor));
    }

    @Test
    public void testPickupWhenNoElevatorAvailable() {
        Elevator elevator1 = new Elevator(1);
        elevator1.setCurrentFloor(1);
        elevatorSystem.addElevator(elevator1);

        Integer callFloor1 = 3;
        Integer direction1 = 1;
        elevatorSystem.pickUp(callFloor1, direction1);
        Integer callFloor2 = 2;
        Integer direction2 = -1;
        elevatorSystem.pickUp(callFloor2, direction2);


        List<Pair<Integer, Integer>> queueWaitingCustomers = elevatorSystem.getQueueWaitingCustomers();
        Assertions.assertEquals(1, queueWaitingCustomers.size());
        Assertions.assertEquals(new Pair<>(callFloor2, direction2), queueWaitingCustomers.get(0));
    }

    @Test
    public void testPickupWithWaitingForElevatorRelease() {
        Elevator elevator = new Elevator(1);
        elevator.setCurrentFloor(1);
        elevatorSystem.addElevator(elevator);

        Integer callFloor1 = 3;
        Integer direction1 = 1;
        elevatorSystem.pickUp(callFloor1, direction1);
        Integer callFloor2 = 2;
        Integer direction2 = -1;
        elevatorSystem.pickUp(callFloor2, direction2);

        String input = "4";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        elevatorSystem.step();
        elevatorSystem.step();
        elevatorSystem.step();


        List<Pair<Integer, Integer>> queueWaitingCustomers = elevatorSystem.getQueueWaitingCustomers();
        Assertions.assertEquals(0, queueWaitingCustomers.size());
        Assertions.assertEquals(ElevatorState.GOING_DOWN, elevator.getElevatorState());
    }

    @Test
    public void testStep() {
        Elevator elevator1 = new Elevator(1);
        elevator1.setCurrentFloor(1);
        elevatorSystem.addElevator(elevator1);

        Elevator elevator2 = new Elevator(2);
        elevator2.setCurrentFloor(5);
        elevatorSystem.addElevator(elevator2);

        elevatorSystem.pickUp(3, 1);
        elevatorSystem.pickUp(3, 1);

        elevatorSystem.step();


        Assertions.assertEquals(2, elevator1.getCurrentFloor());
        Assertions.assertEquals(ElevatorState.GOING_UP, elevator1.getElevatorState());

        Assertions.assertEquals(5, elevator2.getCurrentFloor());
        Assertions.assertEquals(ElevatorState.FREE, elevator2.getElevatorState());
    }

    @Test
    public void testFindElevatorToPickUp_TwoFreeElevatorsDifferentFloorDifference() {
        Elevator elevator1 = new Elevator(1);
        elevator1.setCurrentFloor(1);
        elevatorSystem.addElevator(elevator1);

        Elevator elevator2 = new Elevator(2);
        elevator2.setCurrentFloor(5);
        elevatorSystem.addElevator(elevator2);

        elevatorSystem.pickUp(4, 1);

        Assertions.assertEquals(ElevatorState.GOING_DOWN, elevator2.getElevatorState());
        Assertions.assertEquals(4, elevator2.getCurrentDestinationFloor());

        Assertions.assertEquals(ElevatorState.FREE, elevator1.getElevatorState());
    }

    @Test
    public void testFindElevatorToPickUp_TwoFreeElevatorsSameFloorDifference() {
        Elevator elevator1 = new Elevator(1);
        elevator1.setCurrentFloor(1);
        elevatorSystem.addElevator(elevator1);

        Elevator elevator2 = new Elevator(2);
        elevator2.setCurrentFloor(5);
        elevatorSystem.addElevator(elevator2);

        elevatorSystem.pickUp(3, 1);

        Assertions.assertEquals(ElevatorState.GOING_UP, elevator1.getElevatorState());
        Assertions.assertEquals(3, elevator1.getCurrentDestinationFloor());

        Assertions.assertEquals(ElevatorState.FREE, elevator2.getElevatorState());
    }

    @Test
    public void testFindElevatorToPickUp_TwoElevatorsGoingUp() {
        Elevator elevator1 = new Elevator(1);
        elevator1.setCurrentFloor(1);
        elevatorSystem.addElevator(elevator1);

        Elevator elevator2 = new Elevator(2);
        elevator2.setCurrentFloor(5);
        elevatorSystem.addElevator(elevator2);

        elevatorSystem.pickUp(6, 1);
        elevatorSystem.pickUp(5, 1);
        elevatorSystem.pickUp(10, 1);


        Assertions.assertEquals(ElevatorState.GOING_UP, elevator1.getElevatorState());
        Assertions.assertEquals(ElevatorState.GOING_UP, elevator2.getElevatorState());
        Assertions.assertEquals(2, elevator2.getNumberOfDestinations());
        Assertions.assertTrue(elevator2.hasCall(10));
    }

    @Test
    public void testFindElevatorToPickUp_TwoElevatorsGoingDown() {
        Elevator elevator1 = new Elevator(1);
        elevator1.setCurrentFloor(10);
        elevatorSystem.addElevator(elevator1);

        Elevator elevator2 = new Elevator(2);
        elevator2.setCurrentFloor(15);
        elevatorSystem.addElevator(elevator2);

        elevatorSystem.pickUp(4, -1);
        elevatorSystem.pickUp(9, -1);
        elevatorSystem.pickUp(13, -1);


        Assertions.assertEquals(ElevatorState.GOING_DOWN, elevator1.getElevatorState());
        Assertions.assertEquals(ElevatorState.GOING_DOWN, elevator2.getElevatorState());
        Assertions.assertEquals(2, elevator1.getNumberOfDestinations());
        Assertions.assertTrue(elevator1.hasCall(9));
    }

    @Test
    public void testFindElevatorToPickUp_FreeBetterThanGoingUp() {
        Elevator elevator1 = new Elevator(1);
        elevator1.setCurrentFloor(1);
        elevatorSystem.addElevator(elevator1);

        Elevator elevator2 = new Elevator(2);
        elevator2.setCurrentFloor(10);
        elevatorSystem.addElevator(elevator2);

        elevatorSystem.pickUp(2, 1);

        elevatorSystem.pickUp(6, 1);


        Assertions.assertEquals(ElevatorState.GOING_UP, elevator1.getElevatorState());
        Assertions.assertEquals(ElevatorState.GOING_DOWN, elevator2.getElevatorState());
        Assertions.assertEquals(2, elevator1.getCurrentDestinationFloor());
        Assertions.assertEquals(6, elevator2.getCurrentDestinationFloor());
    }

    @Test
    public void testFindElevatorToPickUp_GoingUpBetterThanFree() {
        Elevator elevator1 = new Elevator(1);
        elevator1.setCurrentFloor(1);
        elevatorSystem.addElevator(elevator1);

        Elevator elevator2 = new Elevator(2);
        elevator2.setCurrentFloor(10);
        elevatorSystem.addElevator(elevator2);

        elevatorSystem.pickUp(2, 1);

        elevatorSystem.pickUp(3, 1);


        Assertions.assertEquals(ElevatorState.GOING_UP, elevator1.getElevatorState());
        Assertions.assertEquals(ElevatorState.FREE, elevator2.getElevatorState());
        Assertions.assertEquals(2, elevator1.getNumberOfDestinations());
    }

    @Test
    public void testFindElevatorToPickUp_FreeBetterThanGoingDown() {
        Elevator elevator1 = new Elevator(1);
        elevator1.setCurrentFloor(10);
        elevatorSystem.addElevator(elevator1);

        Elevator elevator2 = new Elevator(2);
        elevator2.setCurrentFloor(1);
        elevatorSystem.addElevator(elevator2);

        elevatorSystem.pickUp(7, -1);

        elevatorSystem.pickUp(4, -1);


        Assertions.assertEquals(ElevatorState.GOING_DOWN, elevator1.getElevatorState());
        Assertions.assertEquals(ElevatorState.GOING_UP, elevator2.getElevatorState());
        Assertions.assertEquals(7, elevator1.getCurrentDestinationFloor());
        Assertions.assertEquals(4, elevator2.getCurrentDestinationFloor());
    }

    @Test
    public void testFindElevatorToPickUp_GoingDownBetterThanFree() {
        Elevator elevator1 = new Elevator(1);
        elevator1.setCurrentFloor(10);
        elevatorSystem.addElevator(elevator1);

        Elevator elevator2 = new Elevator(2);
        elevator2.setCurrentFloor(1);
        elevatorSystem.addElevator(elevator2);

        elevatorSystem.pickUp(8, -1);

        elevatorSystem.pickUp(7, -1);


        Assertions.assertEquals(ElevatorState.GOING_DOWN, elevator1.getElevatorState());
        Assertions.assertEquals(ElevatorState.FREE, elevator2.getElevatorState());
        Assertions.assertEquals(2, elevator1.getNumberOfDestinations());
    }

    @Test
    public void testFindElevatorToPickUp_GoingUpBetterThanGoingDown() {
        Elevator elevator1 = new Elevator(1);
        elevator1.setCurrentFloor(10);
        elevatorSystem.addElevator(elevator1);

        Elevator elevator2 = new Elevator(2);
        elevator2.setCurrentFloor(1);
        elevatorSystem.addElevator(elevator2);

        elevatorSystem.pickUp(7, -1);
        elevatorSystem.pickUp(4, 1);

        elevatorSystem.pickUp(5, -1);


        Assertions.assertEquals(ElevatorState.GOING_DOWN, elevator1.getElevatorState());
        Assertions.assertEquals(ElevatorState.GOING_UP, elevator2.getElevatorState());
        Assertions.assertEquals(7, elevator1.getCurrentDestinationFloor());
        Assertions.assertEquals(4, elevator2.getCurrentDestinationFloor());
        Assertions.assertEquals(2, elevator1.getNumberOfDestinations());
    }

    @Test
    public void testFindElevatorToPickUp_GoingDownThanGoingUp() {
        Elevator elevator1 = new Elevator(1);
        elevator1.setCurrentFloor(10);
        elevatorSystem.addElevator(elevator1);

        Elevator elevator2 = new Elevator(2);
        elevator2.setCurrentFloor(1);
        elevatorSystem.addElevator(elevator2);

        elevatorSystem.pickUp(7, -1);
        elevatorSystem.pickUp(4, 1);

        elevatorSystem.pickUp(5, 1);


        Assertions.assertEquals(ElevatorState.GOING_DOWN, elevator1.getElevatorState());
        Assertions.assertEquals(ElevatorState.GOING_UP, elevator2.getElevatorState());
        Assertions.assertEquals(7, elevator1.getCurrentDestinationFloor());
        Assertions.assertEquals(4, elevator2.getCurrentDestinationFloor());
        Assertions.assertEquals(2, elevator2.getNumberOfDestinations());
    }
}
