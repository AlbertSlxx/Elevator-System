import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ElevatorSystem mySystem = new ElevatorSystem();

        mySystem.addElevator(new Elevator(1, 1));

        mySystem.pickup(0, -1);
        mySystem.pickup(4, 1);
        mySystem.pickup(5, -1);
        mySystem.pickup(6, 1);
        mySystem.pickup(6, 1);
        mySystem.pickup(6, 1);

        System.out.println(">>WELCOME TO ELEVATOR SYSTEM SIMULATION<<");
        System.out.println("To take one step of simulation press 'S', if you want to quit program press 'Q'");

        Scanner sc = new Scanner(System.in);
        char decision;

        while (true) {
            decision = sc.next().charAt(0);
            decision = Character.toUpperCase(decision);

            if (decision == 'S')
                mySystem.step();
            else if (decision == 'Q')
                break;
            else
                System.out.println("Wrong character, you can only enter S (step) or Q (quit)");
        }
    }
}