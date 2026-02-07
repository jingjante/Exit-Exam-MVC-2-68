package src.model;

import java.time.LocalDate;

public class Assignment {
    private String citizenId;
    private String shelterId;
    private LocalDate arrivalDate;

    public Assignment(String citizenId, String shelterId, LocalDate arrivalDate) {
        this.citizenId = citizenId;
        this.shelterId = shelterId;
        this.arrivalDate = arrivalDate;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getShelterId() {
        return shelterId;
    }

    public void setShelterId(String shelterId) {
        this.shelterId = shelterId;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "citizenId='" + citizenId + '\'' +
                ", shelterId='" + shelterId + '\'' +
                ", arrivalDate=" + arrivalDate +
                '}';
    }
}