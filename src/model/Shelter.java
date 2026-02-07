package src.model;

public class Shelter {
    private String shelterCode;
    private int maxCapacity;
    private String areaRiskLevel; // LOW, MEDIUM, HIGH
    private int currentOccupancy;

    public Shelter(String shelterCode, int maxCapacity, String areaRiskLevel) {
        this.shelterCode = shelterCode;
        this.maxCapacity = maxCapacity;
        this.areaRiskLevel = areaRiskLevel;
        this.currentOccupancy = 0;
    }

    public String getShelterCode() {
        return shelterCode;
    }

    public void setShelterCode(String shelterCode) {
        this.shelterCode = shelterCode;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getAreaRiskLevel() {
        return areaRiskLevel;
    }

    public void setAreaRiskLevel(String areaRiskLevel) {
        this.areaRiskLevel = areaRiskLevel;
    }

    public int getCurrentOccupancy() {
        return currentOccupancy;
    }

    public void setCurrentOccupancy(int currentOccupancy) {
        this.currentOccupancy = currentOccupancy;
    }

    public boolean isFull() {
        return currentOccupancy >= maxCapacity;
    }

    public int getAvailableSpace() {
        return maxCapacity - currentOccupancy;
    }

    public void incrementOccupancy() {
        if (currentOccupancy < maxCapacity) {
            currentOccupancy++;
        }
    }

    public void decrementOccupancy() {
        if (currentOccupancy > 0) {
            currentOccupancy--;
        }
    }

    @Override
    public String toString() {
        return "Shelter{" +
                "code='" + shelterCode + '\'' +
                ", maxCapacity=" + maxCapacity +
                ", riskLevel='" + areaRiskLevel + '\'' +
                ", occupancy=" + currentOccupancy +
                '}';
    }
}