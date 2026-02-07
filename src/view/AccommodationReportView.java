package src.view;

import src.controller.ShelterController;
import src.model.*;

import java.util.List;
import java.util.Scanner;

public class AccommodationReportView {
    private ShelterController controller;
    private Scanner scanner;

    public AccommodationReportView(ShelterController controller, Scanner scanner) {
        this.controller = controller;
        this.scanner = scanner;
    }

    public void display() {
        while (true) {
            System.out.println("\n╔════════════════════════════════════════════╗");
            System.out.println("║       ACCOMMODATION REPORT VIEW            ║");
            System.out.println("╚════════════════════════════════════════════╝");
            System.out.println("1. View Accommodated Citizens");
            System.out.println("2. View Stranded Citizens (Not Yet Accommodated)");
            System.out.println("3. View Complete Report");
            System.out.println("4. Back to Main Menu");
            System.out.print("\nSelect option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewAccommodatedCitizens();
                    break;
                case "2":
                    viewStrandedCitizens();
                    break;
                case "3":
                    viewCompleteReport();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void viewAccommodatedCitizens() {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("ACCOMMODATED CITIZENS REPORT");
        System.out.println("=".repeat(100));

        List<Citizen> accommodated = controller.getAssignedCitizens();

        if (accommodated.isEmpty()) {
            System.out.println("No citizens have been accommodated yet.");
            return;
        }

        System.out.printf("%-10s %-20s %-8s %-15s %-15s %-15s %-15s%n", 
                         "CITIZEN ID", "NAME", "AGE", "CITIZEN TYPE", "HEALTH STATUS", 
                         "SHELTER CODE", "ARRIVAL DATE");
        System.out.println("-".repeat(100));

        for (Citizen citizen : accommodated) {
            Assignment assignment = controller.getAssignmentForCitizen(citizen.getCitizenId());
            if (assignment != null) {
                System.out.printf("%-15s %-25s %-8d %-15s %-15s %-15s %-15s%n",
                                citizen.getCitizenId(),
                                citizen.getName(),
                                citizen.getAge(),
                                citizen.getCitizenType(),
                                citizen.getHealthStatus(),
                                assignment.getShelterId(),
                                assignment.getArrivalDate());
            }
        }

        System.out.println("-".repeat(100));
        System.out.println("Total Accommodated: " + accommodated.size());

        // Statistics
        long priorityAccommodated = accommodated.stream()
            .filter(Citizen::isPriority)
            .count();
        
        long healthRiskAccommodated = accommodated.stream()
            .filter(Citizen::hasHealthRisk)
            .count();

        System.out.println("\nSTATISTICS:");
        System.out.println("Priority Citizens Accommodated (Children/Elderly/At-Risk/VIP): " + priorityAccommodated);
        System.out.println("Citizens with Health Risks Accommodated: " + healthRiskAccommodated);
    }

    private void viewStrandedCitizens() {
        System.out.println("\n" + "=".repeat(120));
        System.out.println("STRANDED CITIZENS REPORT (NOT YET ACCOMMODATED)");
        System.out.println("=".repeat(120));

        List<Citizen> stranded = controller.getUnassignedCitizens();

        if (stranded.isEmpty()) {
            System.out.println("All citizens have been accommodated!");
            return;
        }

        System.out.printf("%-15s %-25s %-8s %-15s %-15s %-15s %-10s%n", 
                         "CITIZEN ID", "NAME", "AGE", "CITIZEN TYPE", "HEALTH STATUS", 
                         "REGISTRATION DATE", "PRIORITY");
        System.out.println("-".repeat(120));

        for (Citizen citizen : stranded) {
            String priority = citizen.isPriority() ? "YES" : "NO";
            System.out.printf("%-15s %-25s %-8d %-15s %-15s %-15s %-10s%n",
                            citizen.getCitizenId(),
                            citizen.getName(),
                            citizen.getAge(),
                            citizen.getCitizenType(),
                            citizen.getHealthStatus(),
                            citizen.getRegistrationDate(),
                            priority);
        }

        System.out.println("-".repeat(120));
        System.out.println("Total Stranded: " + stranded.size());

        // Priority analysis
        long priorityStranded = stranded.stream()
            .filter(Citizen::isPriority)
            .count();
        
        long healthRiskStranded = stranded.stream()
            .filter(Citizen::hasHealthRisk)
            .count();

        long children = stranded.stream()
            .filter(Citizen::isChild)
            .count();
        
        long elderly = stranded.stream()
            .filter(Citizen::isElderly)
            .count();

        System.out.println("\nURGENT ATTENTION NEEDED:");
        System.out.println("Priority Citizens Still Stranded: " + priorityStranded);
        System.out.println("  - Children (< 18 years): " + children);
        System.out.println("  - Elderly (>= 65 years): " + elderly);
        System.out.println("Citizens with Health Risks Still Stranded: " + healthRiskStranded);
    }

    private void viewCompleteReport() {
        System.out.println("\n" + "=".repeat(100));
        System.out.println("COMPLETE ACCOMMODATION REPORT");
        System.out.println("=".repeat(100));

        List<Citizen> allCitizens = controller.getAllCitizens();
        List<Citizen> accommodated = controller.getAssignedCitizens();
        List<Citizen> stranded = controller.getUnassignedCitizens();

        // Overall statistics
        System.out.println("\n>>> OVERALL STATISTICS <<<");
        System.out.println("-".repeat(60));
        System.out.println("Total Registered Citizens: " + allCitizens.size());
        System.out.println("Accommodated: " + accommodated.size());
        System.out.println("Stranded (Not Yet Accommodated): " + stranded.size());
        
        if (allCitizens.size() > 0) {
            double accommodationRate = (accommodated.size() * 100.0) / allCitizens.size();
            System.out.printf("Accommodation Rate: %.2f%%%n", accommodationRate);
        }

        // Shelter capacity overview
        System.out.println("\n>>> SHELTER CAPACITY OVERVIEW <<<");
        System.out.println("-".repeat(60));
        List<Shelter> shelters = controller.getAllShelters();
        int totalCapacity = shelters.stream().mapToInt(Shelter::getMaxCapacity).sum();
        int totalOccupied = shelters.stream().mapToInt(Shelter::getCurrentOccupancy).sum();
        int totalAvailable = totalCapacity - totalOccupied;

        System.out.println("Total Shelter Capacity: " + totalCapacity);
        System.out.println("Currently Occupied: " + totalOccupied);
        System.out.println("Available Spaces: " + totalAvailable);
        System.out.printf("Overall Occupancy Rate: %.2f%%%n", (totalOccupied * 100.0 / totalCapacity));

        // Priority citizens status
        System.out.println("\n>>> PRIORITY CITIZENS STATUS <<<");
        System.out.println("-".repeat(60));
        
        long totalPriority = allCitizens.stream().filter(Citizen::isPriority).count();
        long accommodatedPriority = accommodated.stream().filter(Citizen::isPriority).count();
        long strandedPriority = stranded.stream().filter(Citizen::isPriority).count();

        System.out.println("Total Priority Citizens: " + totalPriority);
        System.out.println("  - Accommodated: " + accommodatedPriority);
        System.out.println("  - Stranded: " + strandedPriority);

        // Health risk status
        System.out.println("\n>>> HEALTH RISK CITIZENS STATUS <<<");
        System.out.println("-".repeat(60));
        
        long totalHealthRisk = allCitizens.stream().filter(Citizen::hasHealthRisk).count();
        long accommodatedHealthRisk = accommodated.stream().filter(Citizen::hasHealthRisk).count();
        long strandedHealthRisk = stranded.stream().filter(Citizen::hasHealthRisk).count();

        System.out.println("Total Citizens with Health Risks: " + totalHealthRisk);
        System.out.println("  - Accommodated: " + accommodatedHealthRisk);
        System.out.println("  - Stranded: " + strandedHealthRisk);

        // Age demographics
        System.out.println("\n>>> AGE DEMOGRAPHICS <<<");
        System.out.println("-".repeat(60));
        
        long totalChildren = allCitizens.stream().filter(Citizen::isChild).count();
        long accommodatedChildren = accommodated.stream().filter(Citizen::isChild).count();
        
        long totalElderly = allCitizens.stream().filter(Citizen::isElderly).count();
        long accommodatedElderly = accommodated.stream().filter(Citizen::isElderly).count();

        System.out.println("Children (< 18 years):");
        System.out.println("  - Total: " + totalChildren);
        System.out.println("  - Accommodated: " + accommodatedChildren);
        System.out.println("  - Stranded: " + (totalChildren - accommodatedChildren));
        
        System.out.println("Elderly (>= 65 years):");
        System.out.println("  - Total: " + totalElderly);
        System.out.println("  - Accommodated: " + accommodatedElderly);
        System.out.println("  - Stranded: " + (totalElderly - accommodatedElderly));

        // Recommendations
        if (stranded.size() > 0) {
            System.out.println("\n>>> RECOMMENDATIONS <<<");
            System.out.println("-".repeat(60));
            if (strandedPriority > 0) {
                System.out.println("URGENT: " + strandedPriority + " priority citizens need immediate accommodation!");
            }
            if (totalAvailable > 0) {
                System.out.println("Available shelter spaces: " + totalAvailable);
                System.out.println("  Action needed: Allocate shelters to stranded citizens.");
            } else {
                System.out.println("No available shelter spaces. Additional shelters needed!");
            }
        } else {
            System.out.println("\n>>> STATUS <<<");
            System.out.println("-".repeat(60));
            System.out.println("All citizens have been successfully accommodated!");
        }
    }
}