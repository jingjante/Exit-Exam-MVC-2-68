package src.controller;

import src.model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ShelterController {
    private List<Shelter> shelters;
    private List<Citizen> citizens;
    private List<Assignment> assignments;

    public ShelterController() {
        this.shelters = new ArrayList<>();
        this.citizens = new ArrayList<>();
        this.assignments = new ArrayList<>();
    }

    public void loadDataFromCSV(String sheltersFile, String citizensFile, String assignmentsFile) {
        loadShelters(sheltersFile);
        loadCitizens(citizensFile);
        loadAssignments(assignmentsFile);
        updateShelterOccupancy();
    }

    private void loadShelters(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    Shelter shelter = new Shelter(parts[0].trim(), 
                                                 Integer.parseInt(parts[1].trim()), 
                                                 parts[2].trim());
                    shelters.add(shelter);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading shelters: " + e.getMessage());
        }
    }

    private void loadCitizens(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    Citizen citizen = new Citizen(
                        parts[0].trim(),
                        parts[1].trim(),
                        Integer.parseInt(parts[2].trim()),
                        parts[3].trim(),
                        LocalDate.parse(parts[4].trim()),
                        parts[5].trim()
                    );
                    citizens.add(citizen);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading citizens: " + e.getMessage());
        }
    }

    private void loadAssignments(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    Assignment assignment = new Assignment(
                        parts[0].trim(),
                        parts[1].trim(),
                        LocalDate.parse(parts[2].trim())
                    );
                    assignments.add(assignment);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading assignments: " + e.getMessage());
        }
    }

    private void updateShelterOccupancy() {
        for (Assignment assignment : assignments) {
            Shelter shelter = findShelterById(assignment.getShelterId());
            if (shelter != null) {
                shelter.incrementOccupancy();
            }
        }
    }

    // Register a new citizen
    public boolean registerCitizen(String citizenId, String name, int age, 
                                   String healthStatus, String citizenType) {
        if (findCitizenById(citizenId) != null) {
            return false;
        }

        Citizen citizen = new Citizen(citizenId, name, age, healthStatus, 
                                     LocalDate.now(), citizenType);
        citizens.add(citizen);
        return true;
    }

    // Allocate shelter to a citizen
    public String allocateShelter(String citizenId) {
        Citizen citizen = findCitizenById(citizenId);
        if (citizen == null) {
            return "Citizen not found.";
        }

        if (isAlreadyAssigned(citizenId)) {
            return "Citizen already has a shelter assignment.";
        }

        Shelter selectedShelter = findSuitableShelter(citizen);
        if (selectedShelter == null) {
            return "No suitable shelter available.";
        }

        Assignment assignment = new Assignment(citizenId, selectedShelter.getShelterCode(), 
                                              LocalDate.now());
        assignments.add(assignment);
        selectedShelter.incrementOccupancy();

        return "Successfully assigned to shelter: " + selectedShelter.getShelterCode();
    }

    private Shelter findSuitableShelter(Citizen citizen) {
        List<Shelter> availableShelters = shelters.stream()
            .filter(s -> !s.isFull())
            .collect(Collectors.toList());

        if (availableShelters.isEmpty()) {
            return null;
        }

        // If citizen has health risk, prioritize low-risk shelters
        if (citizen.hasHealthRisk()) {
            List<Shelter> lowRiskShelters = availableShelters.stream()
                .filter(s -> "LOW".equals(s.getAreaRiskLevel()))
                .collect(Collectors.toList());
            
            if (!lowRiskShelters.isEmpty()) {
                return lowRiskShelters.get(0);
            }
        }

        // Return first available shelter
        return availableShelters.get(0);
    }

    public Citizen findCitizenById(String citizenId) {
        return citizens.stream()
            .filter(c -> c.getCitizenId().equals(citizenId))
            .findFirst()
            .orElse(null);
    }

    public Shelter findShelterById(String shelterId) {
        return shelters.stream()
            .filter(s -> s.getShelterCode().equals(shelterId))
            .findFirst()
            .orElse(null);
    }

    public boolean isAlreadyAssigned(String citizenId) {
        return assignments.stream()
            .anyMatch(a -> a.getCitizenId().equals(citizenId));
    }

    public List<Shelter> getAllShelters() {
        return new ArrayList<>(shelters);
    }

    public List<Citizen> getAllCitizens() {
        return new ArrayList<>(citizens);
    }

    public List<Assignment> getAllAssignments() {
        return new ArrayList<>(assignments);
    }

    public List<Citizen> getCitizensByType(String type) {
        return citizens.stream()
            .filter(c -> c.getCitizenType().equals(type))
            .collect(Collectors.toList());
    }

    public List<Citizen> getAssignedCitizens() {
        Set<String> assignedIds = assignments.stream()
            .map(Assignment::getCitizenId)
            .collect(Collectors.toSet());
        
        return citizens.stream()
            .filter(c -> assignedIds.contains(c.getCitizenId()))
            .collect(Collectors.toList());
    }

    public List<Citizen> getUnassignedCitizens() {
        Set<String> assignedIds = assignments.stream()
            .map(Assignment::getCitizenId)
            .collect(Collectors.toSet());
        
        return citizens.stream()
            .filter(c -> !assignedIds.contains(c.getCitizenId()))
            .collect(Collectors.toList());
    }

    public Assignment getAssignmentForCitizen(String citizenId) {
        return assignments.stream()
            .filter(a -> a.getCitizenId().equals(citizenId))
            .findFirst()
            .orElse(null);
    }
}