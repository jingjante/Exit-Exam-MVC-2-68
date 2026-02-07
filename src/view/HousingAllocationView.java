package src.view;

import src.controller.ShelterController;
import src.model.*;

import java.util.List;
import java.util.Scanner;

public class HousingAllocationView {
    private ShelterController controller;
    private Scanner scanner;

    public HousingAllocationView(ShelterController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    public void display() {
        while (true) {
            System.out.println("\n╔════════════════════════════════════════════╗");
            System.out.println("║       HOUSING ALLOCATION VIEW              ║");
            System.out.println("╚════════════════════════════════════════════╝");
            System.out.println("1. View All Shelters");
            System.out.println("2. View Shelter Details");
            System.out.println("3. Allocate Shelter to Citizen");
            System.out.println("4. Back to Main Menu");
            System.out.print("\nSelect option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewAllShelters();
                    break;
                case "2":
                    viewShelterDetails();
                    break;
                case "3":
                    allocateShelter();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void viewAllShelters() {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("ALL SHELTERS OVERVIEW");
        System.out.println("=".repeat(100));

        List<Shelter> shelters = controller.getAllShelters();

        if (shelters.isEmpty()) {
            System.out.println("No shelters available.");
            return;
        }

        System.out.printf("%-15s %-18s %-18s %-15s %-15s%n", 
                         "SHELTER CODE", "MAX CAPACITY", "CURRENT OCCUPANCY", "AVAILABLE", "RISK LEVEL");
        System.out.println("-".repeat(100));

        for (Shelter shelter : shelters) {
            String status = shelter.isFull() ? "FULL" : String.valueOf(shelter.getAvailableSpace());
            System.out.printf("%-15s %-18d %-18d %-15s %-15s%n",
                            shelter.getShelterCode(),
                            shelter.getMaxCapacity(),
                            shelter.getCurrentOccupancy(),
                            status,
                            shelter.getAreaRiskLevel());
        }

        System.out.println("-".repeat(100));
        
        // Summary statistics
        int totalCapacity = shelters.stream().mapToInt(Shelter::getMaxCapacity).sum();
        int totalOccupied = shelters.stream().mapToInt(Shelter::getCurrentOccupancy).sum();
        int totalAvailable = totalCapacity - totalOccupied;

        System.out.println("\nSUMMARY:");
        System.out.println("Total Shelters: " + shelters.size());
        System.out.println("Total Capacity: " + totalCapacity);
        System.out.println("Currently Occupied: " + totalOccupied);
        System.out.println("Available Spaces: " + totalAvailable);
        System.out.printf("Occupancy Rate: %.2f%%%n", (totalOccupied * 100.0 / totalCapacity));
    }

    private void viewShelterDetails() {
        System.out.print("\nEnter Shelter Code: ");
        String shelterCode = scanner.nextLine().trim();

        Shelter shelter = controller.findShelterById(shelterCode);
        if (shelter == null) {
            System.out.println("Shelter not found.");
            return;
        }

        System.out.println("\n" + "=".repeat(100));
        System.out.println("SHELTER DETAILS: " + shelterCode);
        System.out.println("=".repeat(100));

        System.out.println("Shelter Code: " + shelter.getShelterCode());
        System.out.println("Maximum Capacity: " + shelter.getMaxCapacity());
        System.out.println("Current Occupancy: " + shelter.getCurrentOccupancy());
        System.out.println("Available Spaces: " + shelter.getAvailableSpace());
        System.out.println("Area Risk Level: " + shelter.getAreaRiskLevel());
        System.out.println("Status: " + (shelter.isFull() ? "FULL" : "AVAILABLE"));

        // Display residents
        System.out.println("\n>>> CURRENT RESIDENTS <<<");
        List<Assignment> allAssignments = controller.getAllAssignments();
        
        System.out.printf("%-15s %-25s %-8s %-15s %-15s%n", 
                         "CITIZEN ID", "NAME", "AGE", "HEALTH STATUS", "ARRIVAL DATE");
        System.out.println("-".repeat(100));

        int residentCount = 0;
        for (Assignment assignment : allAssignments) {
            if (assignment.getShelterId().equals(shelterCode)) {
                Citizen citizen = controller.findCitizenById(assignment.getCitizenId());
                if (citizen != null) {
                    System.out.printf("%-15s %-25s %-8d %-15s %-15s%n",
                                    citizen.getCitizenId(),
                                    citizen.getName(),
                                    citizen.getAge(),
                                    citizen.getHealthStatus(),
                                    assignment.getArrivalDate());
                    residentCount++;
                }
            }
        }

        if (residentCount == 0) {
            System.out.println("No residents currently assigned to this shelter.");
        }
        System.out.println("-".repeat(100));
    }

    private void allocateShelter() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("ALLOCATE SHELTER TO CITIZEN");
        System.out.println("=".repeat(80));

        // Show unassigned citizens
        List<Citizen> unassigned = controller.getUnassignedCitizens();
        if (unassigned.isEmpty()) {
            System.out.println("All citizens have been assigned shelters.");
            return;
        }

        System.out.println("\nUNASSIGNED CITIZENS:");
        System.out.printf("%-15s %-25s %-8s %-15s %-15s%n", 
                         "CITIZEN ID", "NAME", "AGE", "HEALTH STATUS", "CITIZEN TYPE");
        System.out.println("-".repeat(95));

        for (Citizen citizen : unassigned) {
            System.out.printf("%-15s %-25s %-8d %-15s %-15s%n",
                            citizen.getCitizenId(),
                            citizen.getName(),
                            citizen.getAge(),
                            citizen.getHealthStatus(),
                            citizen.getCitizenType());
        }
        System.out.println("-".repeat(95));

        System.out.print("\nEnter Citizen ID to allocate: ");
        String citizenId = scanner.nextLine().trim();

        String result = controller.allocateShelter(citizenId);
        System.out.println("\n" + result);

        if (result.startsWith("Successfully")) {
            Citizen citizen = controller.findCitizenById(citizenId);
            Assignment assignment = controller.getAssignmentForCitizen(citizenId);
            if (citizen != null && assignment != null) {
                System.out.println("\nALLOCATION DETAILS:");
                System.out.println("Citizen: " + citizen.getName() + " (ID: " + citizen.getCitizenId() + ")");
                System.out.println("Shelter: " + assignment.getShelterId());
                System.out.println("Arrival Date: " + assignment.getArrivalDate());
            }
        }
    }
}