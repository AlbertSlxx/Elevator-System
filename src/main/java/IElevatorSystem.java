package main.java;

import javafx.util.Pair;

import java.util.TreeMap;

public interface IElevatorSystem {
    void addElevator(Elevator elevator);
    void pickUp(Integer callFloor, Integer direction);
    void update(TreeMap<Pair<Integer, Integer>, Elevator> currentMap, Elevator elevator);
    void step();
    void displayStatusOfSystem();
}
