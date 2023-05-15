import java.util.List;

public interface IElevatorSystem {
    void pickup(Integer callFloor, Integer direction);

    //void update(Integer elevatorID, Integer currentFloor, Integer destinationFloor);

    void step();

    List<Triple<Integer, Integer, Integer>> status();
}
