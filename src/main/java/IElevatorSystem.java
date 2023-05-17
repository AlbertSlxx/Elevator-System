package main.java;

public interface IElevatorSystem {
    void addElevator(Elevator elevator);
    void pickUp(Integer callFloor, Integer direction);
    void step();
    void displayStatusOfSystem();
}
