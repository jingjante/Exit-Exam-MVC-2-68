package src.view;

import src.controller.ShelterController;
import src.model.Citizen;

import java.util.List;
import java.util.Scanner;

public class CitizenRegistrationView {
    private ShelterController controller;
    private Scanner scanner;

    public CitizenRegistrationView(ShelterController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    public void display() {
        while (true) {
            System.out.println("\n╔═════════════════════════════════════════════╗");
            System.out.println("║       CITIZEN REGISTRATION VIEW             ║");
            System.out.println("╚═════════════════════════════════════════════╝");
            System.out.println("1. View All Citizens");
            System.out.println("2. View Citizens by Type");
            System.out.println("3. Register New Citizen");
            System.out.println("4. Back to Main Menu");
            System.out.print("\nSelect option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewAllCitizens();
                    break;
                case "2":
                    viewCitizensByType();
                    break;
                case "3":
                    registerNewCitizen();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void viewAllCitizens() {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("ALL REGISTERED CITIZENS");
        System.out.println("=".repeat(100));

        List<Citizen> allCitizens = controller.getAllCitizens();
        
        if (allCitizens.isEmpty()) {
            System.out.println("No citizens registered yet.");
            return;
        }

        System.out.printf("%-10s %-23s %-8s %-15s %-15s %-15s%n", 
                         "CITIZEN ID", "NAME", "AGE", "HEALTH STATUS", "CITIZEN TYPE", "REGISTRATION DATE");
        System.out.println("-".repeat(100));

        for (Citizen citizen : allCitizens) {
            System.out.printf("%-10s %-23s %-8d %-15s %-15s %-15s%n",
                            citizen.getCitizenId(),
                            citizen.getName(),
                            citizen.getAge(),
                            citizen.getHealthStatus(),
                            citizen.getCitizenType(),
                            citizen.getRegistrationDate());
        }

        System.out.println("-".repeat(100));
        System.out.println("Total Citizens: " + allCitizens.size());
    }

    private void viewCitizensByType() {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("CITIZENS BY TYPE");
        System.out.println("=".repeat(100));

        // Display GENERAL citizens
        System.out.println("\n>>> GENERAL CITIZENS <<<");
        displayCitizenList(controller.getCitizensByType("GENERAL"));

        // Display AT_RISK citizens
        System.out.println("\n>>> AT-RISK CITIZENS <<<");
        displayCitizenList(controller.getCitizensByType("AT_RISK"));

        // Display VIP citizens
        System.out.println("\n>>> VIP CITIZENS <<<");
        displayCitizenList(controller.getCitizensByType("VIP"));
    }

    private void displayCitizenList(List<Citizen> citizens) {
        if (citizens.isEmpty()) {
            System.out.println("No citizens in this category.");
            return;
        }

        System.out.printf("%-10s %-23s %-8s %-15s %-15s%n", 
                         "CITIZEN ID", "NAME", "AGE", "HEALTH STATUS", "REGISTRATION DATE");
        System.out.println("-".repeat(100));

        for (Citizen citizen : citizens) {
            System.out.printf("%-10s %-23s %-8d %-15s %-15s%n",
                            citizen.getCitizenId(),
                            citizen.getName(),
                            citizen.getAge(),
                            citizen.getHealthStatus(),
                            citizen.getRegistrationDate());
        }
        System.out.println("-".repeat(100));
        System.out.println("Count: " + citizens.size());
    }

    private void registerNewCitizen() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("REGISTER NEW CITIZEN");
        System.out.println("=".repeat(80));

        System.out.print("Enter Citizen ID: ");
        String citizenId = scanner.nextLine().trim();

        System.out.print("Enter Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter Age: ");
        int age;
        try {
            age = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid age format. Registration cancelled.");
            return;
        }

        System.out.print("Enter Health Status (HEALTHY/AT_RISK/CRITICAL): ");
        String healthStatus = scanner.nextLine().trim().toUpperCase();

        System.out.print("Enter Citizen Type (GENERAL/AT_RISK/VIP): ");
        String citizenType = scanner.nextLine().trim().toUpperCase();

        boolean success = controller.registerCitizen(citizenId, name, age, healthStatus, citizenType);

        if (success) {
            System.out.println("\nCitizen registered successfully!");
            System.out.println("Citizen ID: " + citizenId);
            System.out.println("Name: " + name);
        } else {
            System.out.println("\nRegistration failed. Citizen ID already exists.");
        }
    }
}