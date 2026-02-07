package src;

import src.view.*;
import src.controller.*;

import java.util.Scanner;

public class EmergencyShelterSystem {
    private ShelterController controller;
    private CitizenRegistrationView citizenView;
    private HousingAllocationView housingView;
    private AccommodationReportView reportView;
    private Scanner scanner;

    public EmergencyShelterSystem() {
        controller = new ShelterController();
        scanner = new Scanner(System.in);
        
        citizenView = new CitizenRegistrationView(controller, scanner);
        housingView = new HousingAllocationView(controller, scanner);
        reportView = new AccommodationReportView(controller, scanner);
    }

    public void start() {
        System.out.println("Loading data from CSV files...");
        controller.loadDataFromCSV("data/shelters.csv", "data/citizens.csv", "data/assignments.csv");
        System.out.println("Data loaded successfully!\n");

        while (true) {
            displayMainMenu();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    citizenView.display();
                    break;
                case "2":
                    housingView.display();
                    break;
                case "3":
                    reportView.display();
                    break;
                case "4":
                    System.out.println("\n" + "=".repeat(60));
                    System.out.println("Thank you for using Emergency Shelter System!");
                    System.out.println("Stay safe!");
                    System.out.println("=".repeat(60));
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("\n");
        System.out.println("╔════════════════════════════════════════════════════════╗");
        System.out.println("║                                                        ║");
        System.out.println("║         EMERGENCY SHELTER ALLOCATION SYSTEM            ║");
        System.out.println("║                                                        ║");
        System.out.println("╚════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("  1. Citizen Registration");
        System.out.println("  2. Housing Allocation");
        System.out.println("  3. Accommodation Reports");
        System.out.println("  4. Exit System");
        System.out.println();
        System.out.print("  Select option: ");
    }

    public static void main(String[] args) {
        EmergencyShelterSystem system = new EmergencyShelterSystem();
        system.start();
    }
}