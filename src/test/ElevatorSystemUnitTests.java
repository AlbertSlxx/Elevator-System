import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.java.*;


import java.util.List;


public class ElevatorSystemUnitTests {
    private ElevatorSystem elevatorSystem;

    @BeforeEach
    public void setup() {
        elevatorSystem = new ElevatorSystem();
    }


    @Test
    public void testPickupWhenNoElevatorAvailable() {
        Integer callFloor = 3;
        Integer direction = 1;

        elevatorSystem.pickup(callFloor, direction);


        List<Pair<Integer, Integer>> queueWaitingCustomers = elevatorSystem.getQueueWaitingCustomers();
        Assertions.assertEquals(1, queueWaitingCustomers.size());
        Assertions.assertEquals(new Pair<>(callFloor, direction), queueWaitingCustomers.get(0));
    }

    @Test
    public void testPickupWhenFreeElevatorAvailable() {
        int callFloor = 3;
        Integer direction = 1;

        Elevator elevator = new Elevator(1);
        elevatorSystem.addElevator(elevator);

        elevatorSystem.pickup(callFloor, direction);


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

        elevatorSystem.pickup(callFloor, direction);


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

        elevatorSystem.pickup(callFloor, direction);


        Assertions.assertTrue(elevator.hasCall(callFloor));
    }

    @Test
    public void testStep() {
        Elevator elevator1 = new Elevator(1);
        elevator1.setCurrentFloor(1);
        elevatorSystem.addElevator(elevator1);

        Elevator elevator2 = new Elevator(2);
        elevator2.setCurrentFloor(5);
        elevatorSystem.addElevator(elevator2);

        elevatorSystem.pickup(3, 1);
        elevatorSystem.pickup(2, 1);

        elevatorSystem.step();


        Assertions.assertEquals(2, elevator1.getCurrentFloor());
        Assertions.assertEquals(ElevatorState.GOING_UP, elevator1.getElevatorState());

        Assertions.assertEquals(4, elevator2.getCurrentFloor());
        Assertions.assertEquals(ElevatorState.GOING_DOWN, elevator2.getElevatorState());
    }
}
