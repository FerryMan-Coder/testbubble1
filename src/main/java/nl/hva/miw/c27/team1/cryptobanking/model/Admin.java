package nl.hva.miw.c27.team1.cryptobanking.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Admin extends User {

    private int staffId;

    private final Logger logger = LoggerFactory.getLogger(Admin.class);

    public Admin(String firstName, String prefix, String surName, int bsnNumber, LocalDate birthDate,
                 String streetName, String houseNumber, String zipCode, String residence, String country,
                 Profile profile, int staffId) {
        super(firstName, null, surName, bsnNumber, birthDate, streetName, houseNumber, zipCode, residence,
                country, profile);
        this.staffId = staffId;
    }

    public Admin(int id, String role) {
        super(id, role);
        logger.info("New Admin with 2 attributes");
    }

    public int getStaffId() {
        return staffId;
    }

    public void setStaffId(int staffId) {
        this.staffId = staffId;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }
}
