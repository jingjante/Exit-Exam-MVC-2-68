package src.model;

import java.time.LocalDate;

public class Citizen {
    private String citizenId;
    private String name;
    private int age;
    private String healthStatus; // HEALTHY, AT_RISK, CRITICAL
    private LocalDate registrationDate;
    private String citizenType; // GENERAL, AT_RISK, VIP

    public Citizen(String citizenId, String name, int age, String healthStatus, 
                   LocalDate registrationDate, String citizenType) {
        this.citizenId = citizenId;
        this.name = name;
        this.age = age;
        this.healthStatus = healthStatus;
        this.registrationDate = registrationDate;
        this.citizenType = citizenType;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getCitizenType() {
        return citizenType;
    }

    public void setCitizenType(String citizenType) {
        this.citizenType = citizenType;
    }

    public boolean isChild() {
        return age < 18;
    }

    public boolean isElderly() {
        return age >= 65;
    }

    public boolean isPriority() {
        return isChild() || isElderly() || "AT_RISK".equals(citizenType) || "VIP".equals(citizenType);
    }

    public boolean hasHealthRisk() {
        return "AT_RISK".equals(healthStatus) || "CRITICAL".equals(healthStatus);
    }

    @Override
    public String toString() {
        return "Citizen{" +
                "id='" + citizenId + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", health='" + healthStatus + '\'' +
                ", type='" + citizenType + '\'' +
                ", registered=" + registrationDate +
                '}';
    }
}