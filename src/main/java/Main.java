package main.java;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // System instance creation
        ElevatorSystem mySystem = new ElevatorSystem();

        // Place to add new elevators (with ID and current floor)
        mySystem.addElevator(new Elevator(1, 1));
        mySystem.addElevator(new Elevator(2, 5));
        mySystem.addElevator(new Elevator(3, 10));


        System.out.println(">> WELCOME TO ELEVATOR SYSTEM SIMULATION <<");
        System.out.println(" 'C' - call elevator to the selected floor\n 'S' - take one step of the simulation\n 'D' - display the current status of the system\n 'Q' - end the simulation and exit the program\n");

        // Place to add initial elevator calls
        mySystem.pickUp(0, 1);


        while (true) {
            char decision = sc.next().charAt(0);
            decision = Character.toUpperCase(decision);

            if (decision == 'S')
                mySystem.step();
            else if (decision == 'D')
                mySystem.displayStatusOfSystem();
            else if (decision == 'C') {
                System.out.println("To call the elevator, enter the number of floor you are interested in");
                int currentFloor = sc.nextInt();
                System.out.println("Type in '1' if you want to go up or '-1' if you want to go down");
                int direction = sc.nextInt();

                mySystem.pickUp(currentFloor, direction);
            }
            else if (decision == 'Q')
                break;
            else
                System.out.println("Wrong character, you can only enter S (step) / D (display_status) / Q (quit)");
        }
    }
}